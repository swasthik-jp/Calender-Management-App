package com.example.calender.repository;

import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query(value = "SELECT id,allocated_room_id FROM meeting WHERE ((?1 BETWEEN start AND end) OR (?2 BETWEEN start AND END))",nativeQuery = true)
    Optional<List<Pair<Long,Long>>> getAllMeetingScheduleForGivenDateRange(Date start, Date end);
    //public Optional<Set<Long>> getAllRoomsForGivenMeetingId();
}
