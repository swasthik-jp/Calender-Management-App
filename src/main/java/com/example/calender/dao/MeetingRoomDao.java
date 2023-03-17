package com.example.calender.dao;

import com.example.calender.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomDao extends JpaRepository<MeetingRoom,Long> {
}
