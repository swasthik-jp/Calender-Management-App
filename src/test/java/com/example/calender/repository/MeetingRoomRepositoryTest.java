package com.example.calender.repository;

import com.example.calender.entity.Employee;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MeetingRoomRepositoryTest {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private TestEntityManager entityManager;

    public Office initOffice(){
        Office office=Office.builder()
                .location("banglore")
                .build();
        return office;
    }


    public MeetingRoom initMeetingRoom(Office office){

        MeetingRoom meetingRoom = MeetingRoom.builder()
                .id(1L)
                .name("MeetingRoomZero")
                .capacity(10)
                .office(office)
                .build();
        return meetingRoom;
    }

    @Test
    void findAllByOfficeId_WhenValidOfficeId_ThenReturnListOfMeetingRooms() {

            Office office=initOffice();

          Office savedOffice=entityManager.merge(office);
        MeetingRoom meetingRoom=initMeetingRoom(savedOffice);
       MeetingRoom savedMeetingRoom=entityManager.merge(meetingRoom);

            Optional<List<Long>> result=meetingRoomRepository.findAllByOfficeId(savedOffice.getId());

            assertFalse(result.isEmpty());
            List<MeetingRoom> meetingRoomList=new ArrayList<>();
            for(Long id:result.get()){
                meetingRoomList.add(meetingRoomRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("meeeting room","id",id)));
            }
            assertEquals(Arrays.asList(savedMeetingRoom),meetingRoomList);

    }
}