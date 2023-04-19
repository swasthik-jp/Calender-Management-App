package com.example.calender.mapper;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.*;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.MeetingRoomService;
import org.junit.Test;
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
public class MeetingMapperTest {


    @Mock
    MeetingRoomService meetingRoomService;

    @Mock
    EmployeeService employeeService;

    @InjectMocks
    MeetingMapper meetingMapper;


    @Test
    public void toDto_WhenValidMeeting_ThenParametersMatch() {


        List<Attendee> attendeesList=new ArrayList<>();
        Employee employee=Employee.builder().email("sample@email.com").build();
        attendeesList.add(Attendee.builder()
                .employee(employee)
                .isAttending(AttendingStatus.PENDING).build());
        Set<Attendee> attendees=new HashSet<>(attendeesList);

        MeetingRoom meetingRoom= Mockito.mock(MeetingRoom.class);
        when(meetingRoom.getId()).thenReturn(1L);

        Meeting meeting=Meeting.builder()
                .id(1L)
                .agenda("Test Agenda")
                .description("Test Description")
                .startTimeStamp(new Date(2023,1,1))
                .endTimeStamp(new Date(2023,1,2))
                .allocatedRoom(meetingRoom)
                .attendees(attendees)
                .host(employee)
                .status(MeetingStatus.PENDING)
                .build();



        MeetingDto meetingDto= meetingMapper.toDto(meeting);
        assertEquals(meeting.getId(),meetingDto.getId());
        assertEquals(meeting.getAgenda(),meetingDto.getAgenda());
        assertEquals(meeting.getAttendees().size(),meetingDto.getAttendees().size());
        assertEquals(meeting.getHost().getEmail(),meetingDto.getHostEmail());

    }

    @Test
    public void toEntity_WhenValidMeetingDtoWithNullID_ThenParametersMatch() {

        String email="sample@email.com";

        List<String> attendiesList= Arrays.asList(
                new String[] { email});
        Set<String> attendies=new HashSet<>(attendiesList);

        Long meetingId=1L;
        MeetingRoom meetingRoom=MeetingRoom.builder()
                .id(meetingId).build();

        when(meetingRoomService.getMeetingRoomById(meetingId)).thenReturn(meetingRoom);

        Employee employee=Employee.builder().email(email).build();
        when(employeeService.getEmployeeByEmail(email)).thenReturn(employee);


        MeetingDto meetingDto=MeetingDto.builder()
                .agenda("Test Agenda")
                .description("Test Description")
                .startTimeStamp(new Date(2023,1,1))
                .endTimeStamp(new Date(2023,1,2))
                .allocatedRoomId(meetingRoom.getId())
                .attendees(attendies)
                .hostEmail(employee.getEmail())
                .status(MeetingStatus.PENDING)
                .build();


        Meeting meeting= meetingMapper.toEntity(meetingDto);
        assertNull(meeting.getId());
        assertEquals(meetingDto.getHostEmail(),meeting.getHost().getEmail());
        assertEquals(meetingDto.getAllocatedRoomId(),meeting.getAllocatedRoom().getId());

        Set<String> attendeesSet=meeting.getAttendees().stream().map(a->a.getEmployee().getEmail()).collect(Collectors.toSet());
        assertEquals(meetingDto.getAttendees(),attendeesSet);
    }

    @Test
    public void toEntity_WhenValidMeetingDtoWithValidID_ThenIDIsNull() {
        String email="sample@email.com";

        List<String> attendiesList= Arrays.asList(
                new String[] { email});
        Set<String> attendies=new HashSet<>(attendiesList);

        Long meetingId=1L;
        MeetingRoom meetingRoom=MeetingRoom.builder()
                .id(meetingId).build();

        when(meetingRoomService.getMeetingRoomById(meetingId)).thenReturn(meetingRoom);

        Employee employee=Employee.builder().email(email).build();
        when(employeeService.getEmployeeByEmail(email)).thenReturn(employee);


        MeetingDto meetingDto=MeetingDto.builder()
                .id(1L)
                .agenda("Test Agenda")
                .description("Test Description")
                .startTimeStamp(new Date(2023,1,1))
                .endTimeStamp(new Date(2023,1,2))
                .allocatedRoomId(meetingRoom.getId())
                .attendees(attendies)
                .hostEmail(employee.getEmail())
                .status(MeetingStatus.PENDING)
                .build();


        Meeting meeting= meetingMapper.toEntity(meetingDto);
        assertNull(meeting.getId());

    }

}