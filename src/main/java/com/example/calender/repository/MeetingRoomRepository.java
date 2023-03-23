package com.example.calender.repository;

import com.example.calender.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom,Long> {
}
