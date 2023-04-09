package com.example.calender.service;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.*;
import com.example.calender.exception.PolicyViolationException;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.EmployeeRepository;
import com.example.calender.repository.MeetingRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MeetingServiceImplTest {

    @Mock
    MeetingRepository meetingRepository;

    @Mock
    MeetingRoomService meetingRoomService;

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    MeetingService meetingService = new MeetingServiceImpl();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
   public void canSchedule_WhenAttendeesGreaterThan6_ThenThrowPolicyViolationException() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");
        attendees.add("email6@mail.com");
        attendees.add("email7@mail.com");


        thrown.expect(PolicyViolationException.class);
        thrown.expectMessage("A");
      meetingService.canSchedule(attendees,new Date(),new Date());
    }

    @Test
    public void canSchedule_WhenTimeLessThan30Mins_ThenThrowPolicyViolationException() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");

        Calendar calendar = Calendar.getInstance();


        calendar.setTime(new Date());
        if(calendar.get(Calendar.HOUR_OF_DAY)<10 || calendar.get(Calendar.HOUR_OF_DAY)>18){
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,0);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }else {
            calendar.add(Calendar.MINUTE, 10);
        }
        Date startDate = calendar.getTime();// set 11:00am start date
        calendar.add(Calendar.MINUTE, 10);//add 10 mins to start date
        Date endDate=calendar.getTime();

        thrown.expect(PolicyViolationException.class);
        thrown.expectMessage("A");
        meetingService.canSchedule(attendees,startDate,endDate);

    }

    @Test
    public void canSchedule_WhenStartHourBefore10am_ThenThrowPolicyViolationException() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,5);
        calendar.set(Calendar.MINUTE,10);
        Date startDate=calendar.getTime();// todays date with time 5:10am

        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);//add 1 hour to  start time
        Date endDate=calendar.getTime();


        thrown.expect(PolicyViolationException.class);
        thrown.expectMessage("B");
        meetingService.canSchedule(attendees,startDate,endDate);

    }

    @Test
    public void canSchedule_WhenEndTimeAfter18pm_ThenThrowPolicyViolationException() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        if(calendar.get(Calendar.HOUR_OF_DAY)<10 || calendar.get(Calendar.HOUR_OF_DAY)>18){
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,0);
            calendar.add(Calendar.DAY_OF_MONTH,1); // next day 11:00 am
        }else {
            calendar.add(Calendar.MINUTE, 10); // current time + 10mins
        }
        Date startDate=calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY,20);
        calendar.set(Calendar.MINUTE,10);
        Date endDate=calendar.getTime();// todays date with time 20:10pm


        thrown.expect(PolicyViolationException.class);
        thrown.expectMessage("B");
        meetingService.canSchedule(attendees,startDate,endDate);
    }

    @Test
    public void canSchedule_WhenEndTimeBeforeStartTime_ThenThrowPolicyViolationException() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        if(calendar.get(Calendar.HOUR_OF_DAY)<10 || calendar.get(Calendar.HOUR_OF_DAY)>18){
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,0);
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }else {
            calendar.add(Calendar.MINUTE, 10);
        }
        Date endDate=calendar.getTime();// set 11:00am start date



        calendar.setTime(endDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);//add 1 hour to  end time
        Date startDate=calendar.getTime();

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("End Date Cannot be before Start Date!");
        meetingService.canSchedule(attendees,startDate,endDate);

    }

    @Test
    public void canSchedule_WhenStartTimeBeforeCurrentTime_ThenThrowPolicyViolationException() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        if(calendar.get(Calendar.HOUR_OF_DAY)<10 || calendar.get(Calendar.HOUR_OF_DAY)>18){
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-1);
        }else {
            calendar.add(Calendar.MINUTE, 10);
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-1);
        }// set time to 1hour before
        Date startDate=calendar.getTime();

        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);//add 1 hour to  start time
        Date endDate=calendar.getTime();

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Can't schedule meeting in `Past`!");
        meetingService.canSchedule(attendees,startDate,endDate);
    }


    @Test
    public void canSchedule_WhenNoMeetingIsScheduledInGivenTime_ThenReturnTrue() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        if(calendar.get(Calendar.HOUR_OF_DAY)<10 || calendar.get(Calendar.HOUR_OF_DAY)>18){
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,0);
            calendar.add(Calendar.DAY_OF_MONTH,1); // next day 11:00 am
        }else {
            calendar.add(Calendar.MINUTE, 10);// current time+10mins
        }
        Date startDate=calendar.getTime();

        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);//add 1 hour to  end time
        Date endDate=calendar.getTime();

        Optional<List<List<Long>>> optional=Optional.empty();

        when(meetingRepository.getAllMeetingAndRoomIdForGivenDateRange(startDate,endDate)).thenReturn(optional);

        when(meetingRoomService.getAllMeetingRooms()).thenReturn(Arrays.asList(initMeetingRoom(initOffice())));

       assertTrue(meetingService.canSchedule(attendees,startDate,endDate));

    }


    @Test
    public void canSchedule_WhenMeetingIsScheduledInGivenTime_ThenReturnFreeMeetingRooms() {

        Set<String> attendees=new HashSet<>();
        attendees.add("email1@mail.com");
        attendees.add("email2@mail.com");
        attendees.add("email3@mail.com");
        attendees.add("email4@mail.com");
        attendees.add("email5@mail.com");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        if(calendar.get(Calendar.HOUR_OF_DAY)<10 || calendar.get(Calendar.HOUR_OF_DAY)>18){
            calendar.set(Calendar.HOUR_OF_DAY,11);
            calendar.set(Calendar.MINUTE,0);
            calendar.add(Calendar.DAY_OF_MONTH,1); // next day 11:00 am
        }else {
            calendar.add(Calendar.MINUTE, 10);// current time+10mins
        }
        Date startDate=calendar.getTime();

        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);//add 1 hour to  end time
        Date endDate=calendar.getTime();

        List<List<Long>> meetingIds=Arrays.asList(Arrays.asList(0L,1L));
        Optional<List<List<Long>>> optional=Optional.of(meetingIds);

        when(meetingRepository.getAllMeetingAndRoomIdForGivenDateRange(startDate,endDate)).thenReturn(optional);

        MeetingRoom meetingRoom1=initMeetingRoom(initOffice());
        meetingRoom1.setId(1L);
        MeetingRoom meetingRoom2=initMeetingRoom(initOffice());
        meetingRoom2.setId(2L);

        when(meetingRoomService.getAllMeetingRooms()).thenReturn(Arrays.asList(meetingRoom1,meetingRoom2));
        when(employeeService.getEmployeeByEmail(Mockito.any())).thenReturn(initEmployee(initOffice()));

        assertTrue(meetingService.canSchedule(attendees,startDate,endDate));

    }

    @Test
   public void scheduleMeeting() {
    }

    @Test
   public void changeMeetingStatus_WhenValidMeetingID_ThenChangeStatus() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        MeetingStatus updatedMeetingStatus=MeetingStatus.COMPLETED;

        Optional<Meeting> optional=Optional.of(meeting);
        when(meetingRepository.findById(meeting.getId())).thenReturn(optional);
        when(meetingRepository.save(meeting)).thenReturn(meeting);

        assertEquals(updatedMeetingStatus, meetingService.changeMeetingStatus(meeting.getId(),updatedMeetingStatus));
    }

    @Test
    public void changeMeetingStatus_WhenInValidId_ThenCatchResourceNotFoundException() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        MeetingStatus updatedMeetingStatus=MeetingStatus.COMPLETED;

        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.empty());
        when(meetingRepository.save(meeting)).thenReturn(meeting);

        thrown.expect(ResourceNotFoundException.class);
       meetingService.changeMeetingStatus(meeting.getId(),updatedMeetingStatus);

    }

    @Test
    public void getMeetingDetails_WhenValidId_ThenReturnMeeting() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);


        Optional<Meeting> optional=Optional.of(meeting);
        when(meetingRepository.findById(meeting.getId())).thenReturn(optional);


        meetingService.getMeetingDetails(meeting.getId());
    }

    @Test
    public void getMeetingDetails_WhenInValidId_ThenThrowResourceNotFoundException() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.empty());

        thrown.expect(ResourceNotFoundException.class);
        meetingService.getMeetingDetails(meeting.getId());
    }

    @Test
    public void getMeetingsInParticularWeek_WhenValidSignAndWeek_ThenReturnMeetings() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        when(meetingRepository.getAllMeetingForCurrentWeek()).thenReturn(Optional.of(Arrays.asList(meeting)));
        when(meetingRepository.getAllMeetingForNextParticularWeek(Mockito.anyInt())).thenReturn(Optional.of(Arrays.asList(meeting)));
        when(meetingRepository.getAllMeetingForPastParticularWeek(Mockito.anyInt())).thenReturn(Optional.of(Arrays.asList(meeting)));

        assertEquals(meeting.getId(),meetingService.getMeetingsInParticularWeek('+',0).get(0).getId());
        assertEquals(meeting.getId(),meetingService.getMeetingsInParticularWeek('+',1).get(0).getId());
        assertEquals(meeting.getId(),meetingService.getMeetingsInParticularWeek('-',0).get(0).getId());

    }

    @Test
    public void getMeetingsInParticularWeek_WhenInValidSignAndWeek_ThenThrowIllegalArgumentException() {
      thrown.expect(IllegalArgumentException.class);
      meetingService.getMeetingsInParticularWeek('/',1);
    }

    @Test
   public void getParticularEmployeeMeetings() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);
        Employee employee=initEmployee(office);

        assertEquals(Arrays.asList(meeting),meetingService.getParticularEmployeeMeetings(Arrays.asList(meeting),employee.getId()));

    }

    @Test
   public void setAttendeeStatus() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        AttendingStatus updatedAttendeeStatus=AttendingStatus.YES;

        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));
        when(meetingRepository.save(meeting)).thenReturn(meeting);

        assertEquals(updatedAttendeeStatus,meetingService.setAttendeeStatus(meeting.getId(),initEmployee(office).getId(),updatedAttendeeStatus));

    }

    @Test
    public void setAttendeeStatus_WhenMeetingOrEmployeeNotPresent_ThenThrowResourceNotFoundException() {
        Office office=initOffice();
        Meeting meeting=initMeeting(office);

        AttendingStatus updatedAttendeeStatus=AttendingStatus.YES;

        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.empty());
        when(meetingRepository.save(meeting)).thenReturn(meeting);

        thrown.expect(ResourceNotFoundException.class);
        assertEquals(updatedAttendeeStatus,meetingService.setAttendeeStatus(meeting.getId(),initEmployee(office).getId(),updatedAttendeeStatus));

        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));


        Long invalidEmpId=100L;
        assertEquals(updatedAttendeeStatus,meetingService.setAttendeeStatus(meeting.getId(),invalidEmpId,updatedAttendeeStatus));

    }
}