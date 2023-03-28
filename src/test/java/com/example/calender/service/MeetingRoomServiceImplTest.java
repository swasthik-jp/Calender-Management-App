package com.example.calender.service;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.MeetingRoomRepository;
import org.junit.*;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MeetingRoomServiceImplTest {


    @Mock
    MeetingRoomRepository meetingRoomRepository;
    @InjectMocks
    MeetingRoomService meetingRoomService = new MeetingRoomServiceImpl();


    @Test(expected = ResourceNotFoundException.class)
    public void getMeetingRoomsById_WhenInvalidIdIsGiven_ThenThrowResourceNotFoundException() throws ResourceNotFoundException {
        when(meetingRoomRepository.findById(anyLong())).thenReturn(Optional.empty());
        meetingRoomService.getMeetingRoomById(12);
    }

    @Test
    public void getMeetingRoomsById_WhenValidIdIsGiven_ThenGetMeetingRoomObject() throws ResourceNotFoundException {
        MeetingRoom tempObj = MeetingRoom.builder()
                .id(12L).name("Chaos Genius").capacity(6).isOperational(Boolean.TRUE)
                .build();

        when(meetingRoomRepository.findById(12L)).thenReturn(Optional.of(tempObj));

        assertEquals("Chaos Genius",meetingRoomService.getMeetingRoomById(12).getName());
        assertTrue(meetingRoomService.getMeetingRoomById(12).isOperational());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateMeetingRoom_WhenInvalidIdIsGiven_ThenThrow() throws ResourceNotFoundException {
        when(meetingRoomRepository.findById(12L)).thenReturn(Optional.empty());
        meetingRoomService.updateMeetingRoom(null,12L);


    }
    @Test
    public void updateMeetingRoom_WhenValidIdIsGiven_ThenUpdate() throws ResourceNotFoundException {
        MeetingRoom tempObj = MeetingRoom.builder()
                .id(12L).name("Yet to decided").capacity(0).isOperational(Boolean.FALSE)
                .build();
        MeetingRoom updatedObj = MeetingRoom.builder()
                .id(12L).name("Chaos Genius").capacity(6).isOperational(Boolean.TRUE)
                .build();

        when(meetingRoomRepository.findById(12L)).thenReturn(Optional.of(tempObj));
        when(meetingRoomRepository.save(updatedObj)).thenReturn(updatedObj);

        assertEquals(updatedObj,meetingRoomService.updateMeetingRoom(updatedObj,12));


    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteMeetingRoom_WhenInvalidId_ThenThrow() throws ResourceNotFoundException {
        when(meetingRoomRepository.findById(12L)).thenReturn(Optional.empty());
        meetingRoomService.deleteMeetingRoom(12L);

    }


    @Test(expected = ResourceNotFoundException.class)
    public void getMeetingRoomsByOfficeId_WhenInvalidOfficeId_ThenThrow() throws ResourceNotFoundException {
        when(meetingRoomRepository.findAllByOfficeId(anyLong())).thenReturn(Optional.empty());
        meetingRoomService.getMeetingRoomsByOfficeId(anyLong());
    }

    @Test
    public void getMeetingRoomsByOfficeId_WhenValidOfficeIdWithNoMeetingRoom_ThenReturnNull() throws ResourceNotFoundException {
        when(meetingRoomRepository.findAllByOfficeId(anyLong())).thenReturn(Optional.of(new ArrayList<>(0)));
        assertEquals(new ArrayList<Long>(), meetingRoomService.getMeetingRoomsByOfficeId(anyLong()));
    }

    @Test
    public void getMeetingRoomsByOfficeId_WhenValidOfficeIdWithManyMeetingRoom_ThenReturnListOfMeetingRoomIds() throws ResourceNotFoundException {
        List<Long> idList = new ArrayList<>();
        idList.add(12L);idList.add(24L);
        when(meetingRoomRepository.findAllByOfficeId(anyLong())).thenReturn(Optional.of(List.of(12L,24L)));
        assertEquals(idList, meetingRoomService.getMeetingRoomsByOfficeId(anyLong()));
    }

}