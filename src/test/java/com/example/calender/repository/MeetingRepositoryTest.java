package com.example.calender.repository;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MeetingRepositoryTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private TestEntityManager entityManager;

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




    @Test
    void getAllMeetingAndRoomIdForGivenDateRange_WhenValidRange_ThenReturnMeetingAndRoomId() {
        Office office=initOffice();
        Office  savedOffice= entityManager.merge(office);

        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=entityManager.merge(employee);

        MeetingRoom meetingRoom=initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom=entityManager.merge(meetingRoom);

        Attendee attendee= Attendee.builder()
                .employee(savedEmployee).build();
        Attendee savedAttendee=entityManager.merge(attendee);

        Meeting meeting=initMeeting(savedOffice);
        meeting.setAllocatedRoom(savedMeetingRoom);
        meeting.setAttendees(new HashSet<>(Arrays.asList(savedAttendee)));
        Meeting savedMeeting=entityManager.merge(meeting);

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)-1);
        Date startDate=calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,2);
        Date endDate=calendar.getTime();

        Optional<List<List<Long>>> result=meetingRepository.getAllMeetingAndRoomIdForGivenDateRange(startDate,endDate);

        for(List<Long> ls:result.get()){
            assertEquals(ls.get(0),savedMeeting.getId());
            assertEquals(ls.get(1),savedMeetingRoom.getId());
        }

    }

    @Test
    void getAllMeetingForCustomDateRange_WhenValidRange_ThenReturnMeetings() {
        Office office=initOffice();
        Office  savedOffice= entityManager.merge(office);

        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=entityManager.merge(employee);

        MeetingRoom meetingRoom=initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom=entityManager.merge(meetingRoom);

        Attendee attendee= Attendee.builder()
                .employee(savedEmployee).build();
        Attendee savedAttendee=entityManager.merge(attendee);

        Meeting meeting=initMeeting(savedOffice);
        meeting.setAllocatedRoom(savedMeetingRoom);
        meeting.setAttendees(new HashSet<>(Arrays.asList(savedAttendee)));
        Meeting savedMeeting=entityManager.merge(meeting);

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)-1);
        Date startDate=calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,2);
        Date endDate=calendar.getTime();

        Optional<List<Meeting>> result=meetingRepository.getAllMeetingForCustomDateRange(startDate,endDate);

        for(Meeting curMeeting:result.get()){
            assertEquals(curMeeting.getId(),savedMeeting.getId());
        }

    }

    @Test
    void getAllMeetingForCurrentWeek_ReturnMeetings() {

        Office office=initOffice();
        Office  savedOffice= entityManager.merge(office);

        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=entityManager.merge(employee);

        MeetingRoom meetingRoom=initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom=entityManager.merge(meetingRoom);

        Attendee attendee= Attendee.builder()
                .employee(savedEmployee).build();
        Attendee savedAttendee=entityManager.merge(attendee);

        Meeting meeting=initMeeting(savedOffice);
        meeting.setAllocatedRoom(savedMeetingRoom);
        meeting.setAttendees(new HashSet<>(Arrays.asList(savedAttendee)));
        Meeting savedMeeting=entityManager.merge(meeting);

        Optional<List<Meeting>> result=meetingRepository.getAllMeetingForCurrentWeek();

        for(Meeting curMeeting:result.get()){
            assertEquals(curMeeting.getId(),savedMeeting.getId());
        }
    }

    @Test
    void getAllMeetingForNextParticularWeek_ReturnMeetings() {

        Office office=initOffice();
        Office  savedOffice= entityManager.merge(office);

        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=entityManager.merge(employee);

        MeetingRoom meetingRoom=initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom=entityManager.merge(meetingRoom);

        Attendee attendee= Attendee.builder()
                .employee(savedEmployee).build();
        Attendee savedAttendee=entityManager.merge(attendee);

        Meeting meeting=initMeeting(savedOffice);
        meeting.setAllocatedRoom(savedMeetingRoom);
        meeting.setAttendees(new HashSet<>(Arrays.asList(savedAttendee)));


        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.WEEK_OF_YEAR,calendar.get(Calendar.WEEK_OF_YEAR)+1);
        Date startDate=calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,2);
        Date endDate=calendar.getTime();


        meeting.setStartTimeStamp(startDate);
        meeting.setEndTimeStamp(endDate);
        Meeting savedMeeting=entityManager.merge(meeting);

        Optional<List<Meeting>> result=meetingRepository.getAllMeetingForNextParticularWeek(1);

        for(Meeting curMeeting:result.get()){
            assertEquals(curMeeting.getId(),savedMeeting.getId());
        }
    }

    @Test
    void getAllMeetingForPastParticularWeek_ReturnMeetings() {

        Office office=initOffice();
        Office  savedOffice= entityManager.merge(office);

        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=entityManager.merge(employee);

        MeetingRoom meetingRoom=initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom=entityManager.merge(meetingRoom);

        Attendee attendee= Attendee.builder()
                .employee(savedEmployee).build();
        Attendee savedAttendee=entityManager.merge(attendee);

        Meeting meeting=initMeeting(savedOffice);
        meeting.setAllocatedRoom(savedMeetingRoom);
        meeting.setAttendees(new HashSet<>(Arrays.asList(savedAttendee)));


        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.WEEK_OF_YEAR,calendar.get(Calendar.WEEK_OF_YEAR)-1);
        Date startDate=calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY,2);
        Date endDate=calendar.getTime();


        meeting.setStartTimeStamp(startDate);
        meeting.setEndTimeStamp(endDate);
        Meeting savedMeeting=entityManager.merge(meeting);

        Optional<List<Meeting>> result=meetingRepository.getAllMeetingForPastParticularWeek(1);

        for(Meeting curMeeting:result.get()){
            assertEquals(curMeeting.getId(),savedMeeting.getId());
        }
    }
}