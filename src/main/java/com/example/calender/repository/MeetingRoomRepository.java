package com.example.calender.repository;

import com.example.calender.entity.Employee;
import com.example.calender.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom,Long> {

    @Query(value = "SELECT id FROM meeting_room WHERE office_id=?",nativeQuery = true)
    Optional<List<Long>> findAllByOfficeId(Long officeId);
}
