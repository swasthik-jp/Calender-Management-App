package com.example.calender.service;

import com.example.calender.entity.MeetingRoom;

import java.util.List;

public interface MeetingRoomService {

    MeetingRoom saveMeetingRoom(MeetingRoom meetingRoom);
    List<MeetingRoom> getAllMeetingRooms();
    MeetingRoom getMeetingRoomById(long id);
    MeetingRoom updateMeetingRoom(MeetingRoom meetingRoom,long id);
    void deleteMeetingRoom(long id);
}
