package com.example.calender.controller;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.dto.*;
import com.example.calender.entity.*;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.MeetingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.SpyBeans;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MeetingController.class)
@AutoConfigureMockMvc(webClientEnabled = false,addFilters  = false)
class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingService meetingService;

    @MockBean
    private Mapper<Meeting, MeetingDto> meetingDtoMapper;


    public Office initOffice(){
        Office office=Office.builder()
                .id(1L)
                .build();
        return  office;
    }

    public MeetingRoom initMeetingRoom(Office office){
        MeetingRoom meetingRoom = MeetingRoom.builder()
                .id(10L)
                .name("MeetingRoomZero")
                .capacity(10)
                .office(office)
                .build();
        return meetingRoom;
    }

    public Employee initEmployee(Office office){

        Employee testEmp = Employee.builder()
                .id(10L)
                .name("employee1")
                .email("mail@email.com")
                .office(office)
                .build();
        return testEmp;
    }


    public Meeting initMeeting(Office office){

        Date startdate=new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startdate);
        calendar.add(Calendar.MINUTE,10);
        startdate=calendar.getTime();// add 10 minutes to start date

        calendar.add(Calendar.HOUR_OF_DAY, 1);//add 1 hour to start date
        Date endDate=calendar.getTime();

        Meeting meeting = Meeting.builder()
                .id(10L)
                .startTimeStamp(startdate)
                .endTimeStamp(endDate)
                .allocatedRoom(initMeetingRoom(office))
                .attendees(new HashSet<>(Arrays.asList(Attendee.builder().id(1L).employee(initEmployee(office)).build())))
                .build();
        return meeting;
    }

    public MeetingDto initMeetingDto(Meeting meeting){
        MeetingDto meetingDto=MeetingDto.builder()
                .id(meeting.getId())
                .startTimeStamp(meeting.getStartTimeStamp())
                .endTimeStamp(meeting.getEndTimeStamp())
                .allocatedRoomId(meeting.getAllocatedRoom().getId())
                .attendees(meeting.getAttendees().stream().map(a->a.getEmployee().getEmail()).collect(Collectors.toSet()))
                .build();
        return meetingDto;
    }

    public EmployeeDto initEmployeeDto(){

        OfficeDto officeDto=OfficeDto.builder()
                .id(1L)
                .build();

        EmployeeDto employeeDto=EmployeeDto.builder()
                .name("employee1")
                .email("mail@email.com")
                .office(officeDto)
                .build();
        return employeeDto;
    }

    @Test
    void getMeetingDetails_WhenValidId_ThenReturnMeeting() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        Mockito.when(meetingService.getMeetingDetails(meeting.getId())).thenReturn(meeting);
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));


        Date date = meeting.getStartTimeStamp();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(date);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting/"+meeting.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.start", CoreMatchers.is(strDate)));

    }

    @Test
    void getMeetingDetails_WhenInValidId_ThenExpectNotFoundStatus() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        Mockito.when(meetingService.getMeetingDetails(meeting.getId())).thenThrow(ResourceNotFoundException.class);
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));

        Date date = meeting.getStartTimeStamp();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(date);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting/"+meeting.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void scheduleMeeting_WhenValidMeeting_ThenReturnMeetingId() throws Exception {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        MeetingDto meetingDto=initMeetingDto(meeting);

        Mockito.when(meetingDtoMapper.toEntity(meetingDto)).thenReturn(meeting);
        Mockito.when(meetingService.scheduleMeeting(meeting)).thenReturn(meeting.getId());



        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
 }

    @Test
    void scheduleMeeting_WhenInValidMeeting_ThenExpect_REQUESTED_RANGE_NOT_SATISFIABLE_Status() throws Exception {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        MeetingDto meetingDto=initMeetingDto(meeting);

        Mockito.when(meetingDtoMapper.toEntity(meetingDto)).thenReturn(meeting);
        Mockito.when(meetingService.scheduleMeeting(meeting)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void canSchedule_WhenValidParameters_ThenReturnScheduleResult() throws Exception {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        CanScheduleDto canScheduleDto=new CanScheduleDto();
        canScheduleDto.setStart(meeting.getStartTimeStamp());
        canScheduleDto.setEnd(meeting.getEndTimeStamp());
        canScheduleDto.setAttendees(meeting.getAttendees().stream().map(a->a.getEmployee().getEmail()).collect(Collectors.toSet()));

        Mockito.when(meetingService.canSchedule(Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/can-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(canScheduleDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void cancelMeeting_WhenValidId_ThenCancelMeeting() throws Exception {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        Mockito.when(meetingService.changeMeetingStatus(meeting.getId(), MeetingStatus.CANCELLED)).thenReturn(MeetingStatus.CANCELLED);
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));

        Date date = meeting.getStartTimeStamp();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(date);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cancel-meeting/"+meeting.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @Test
    void cancelMeeting_WhenInValidId_ThenExpectNotFoundStatus() throws Exception {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        Mockito.when(meetingService.changeMeetingStatus(meeting.getId(), MeetingStatus.CANCELLED)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));

        Date date = meeting.getStartTimeStamp();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(date);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/cancel-meeting/"+meeting.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

    }

    @Test
    void setAttendeeStatus_WhenValidAttendee_ThenSetStatus() throws Exception {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.setAttendeeStatus(meeting.getId(),employee.getId(), AttendingStatus.YES)).thenReturn(AttendingStatus.YES);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/attendee-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new AttendeeDto(1L,employee.getId(),AttendingStatus.YES)))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void setAttendeeStatus_WhenInValidAttendee_ThenExpectNotFoundStatus()throws Exception {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.setAttendeeStatus(meeting.getId(),employee.getId(), AttendingStatus.YES)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/attendee-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new AttendeeDto(1L,employee.getId(),AttendingStatus.YES)))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void meetingForEmployee_WhenOnlyIdIsProvided_ThenReturnCurrentDayMeetings() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.getMeetingsInCustomRange(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingService.getParticularEmployeeMeetings(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting")
                        .param("id",employee.getId()+"")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].allocatedRoomId", CoreMatchers.is(meeting.getAllocatedRoom().getId().intValue())));

    }

    @Test
    void meetingForEmployee_WhenStartAndEndDateIsProvided_ThenReturnMeetings() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.getMeetingsInCustomRange(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingService.getParticularEmployeeMeetings(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_WEEK,1);
        Date tommorrowdate=calendar.getTime();// add 10 minutes to start date

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String todayDate = dateFormat.format(new Date());
        String tommorrowDate=dateFormat.format(tommorrowdate);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting")
                        .param("id",employee.getId()+"")
                        .param("start",todayDate)
                        .param("end",tommorrowDate)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].allocatedRoomId", CoreMatchers.is(meeting.getAllocatedRoom().getId().intValue())));

    }


    @Test
    void meetingForEmployee_WhenFilterIsProvided_ThenReturnMeetings() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.getMeetingsInParticularWeek(Mockito.anyChar(),Mockito.anyInt())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingService.getParticularEmployeeMeetings(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));



        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting")
                        .param("id",employee.getId()+"")
                        .param("filter","last_week")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].allocatedRoomId", CoreMatchers.is(meeting.getAllocatedRoom().getId().intValue())));

    }

    @Test
    void meetingForEmployee_WhenFilterCurrent_WeekIsProvided_ThenReturnMeetings() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.getMeetingsInParticularWeek(Mockito.anyChar(),Mockito.anyInt())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingService.getParticularEmployeeMeetings(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));



        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting")
                        .param("id",employee.getId()+"")
                        .param("filter","current_week")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].allocatedRoomId", CoreMatchers.is(meeting.getAllocatedRoom().getId().intValue())));

    }

    @Test
    void meetingForEmployee_WhenFilterNext_Week_WeekIsProvided_ThenReturnMeetings() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.getMeetingsInParticularWeek(Mockito.anyChar(),Mockito.anyInt())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingService.getParticularEmployeeMeetings(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));



        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting")
                        .param("id",employee.getId()+"")
                        .param("filter","next_week")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].allocatedRoomId", CoreMatchers.is(meeting.getAllocatedRoom().getId().intValue())));

    }

    @Test
    void meetingForEmployee_WhenInValidFilter_WeekIsProvided_ThenReturnMeetings() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.getMeetingsInParticularWeek(Mockito.anyChar(),Mockito.anyInt())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingService.getParticularEmployeeMeetings(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));



        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting")
                        .param("id",employee.getId()+"")
                        .param("filter","invalidFilter")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void meetingForEmployee_WhenOnlyStartOrEndDateIsProvided_ThenReturnCurrentDayMeetings() throws Exception {

        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        Mockito.when(meetingService.getMeetingsInCustomRange(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingService.getParticularEmployeeMeetings(Mockito.any(),Mockito.any())).thenReturn(Arrays.asList(meeting));
        Mockito.when(meetingDtoMapper.toDto(meeting)).thenReturn(initMeetingDto(meeting));

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String todayDate = dateFormat.format(new Date());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meeting")
                        .param("id",employee.getId()+"")
                        .param("start",todayDate)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].allocatedRoomId", CoreMatchers.is(meeting.getAllocatedRoom().getId().intValue())));

    }




}