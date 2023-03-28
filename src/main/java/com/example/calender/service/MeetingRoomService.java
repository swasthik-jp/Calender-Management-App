package com.example.calender.service;

import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;

import java.util.List;

public interface MeetingRoomService {

    MeetingRoom saveMeetingRoom(MeetingRoom meetingRoom) throws ResourceAlreadyExistsException;

    List<MeetingRoom> getAllMeetingRooms();

    MeetingRoom getMeetingRoomById(long id) throws ResourceNotFoundException;

    MeetingRoom updateMeetingRoom(MeetingRoom meetingRoom, long id) throws ResourceNotFoundException;

    void deleteMeetingRoom(long id) throws ResourceNotFoundException;

    List<Long> getMeetingRoomsByOfficeId(long id) throws ResourceNotFoundException;
}
