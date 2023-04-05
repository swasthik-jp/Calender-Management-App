package com.example.calender.repository;

import com.example.calender.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    @Query(value = "SELECT id,allocated_room_id FROM meeting WHERE ((?1 BETWEEN start AND end) OR (?2 BETWEEN start AND END))", nativeQuery = true)
    Optional<List<List<Long>>> getAllMeetingAndRoomIdForGivenDateRange(Date start, Date end);
    //public Optional<Set<Long>> getAllRoomsForGivenMeetingId();

    @Query(value = "SELECT * FROM meeting WHERE start > ?1 AND end < ?2 ",nativeQuery = true)
    Optional<List<Meeting>> getAllMeetingForCustomDateRange(Date start,Date end);

    @Query(value = "SELECT * FROM meeting WHERE WEEK(start) = WEEK(now())",nativeQuery = true)
    Optional<List<Meeting>> getAllMeetingForCurrentWeek();

    @Query(value = "SELECT * FROM meeting WHERE WEEK(start) = WEEK(now() + interval ? week)",nativeQuery = true)
    Optional<List<Meeting>> getAllMeetingForNextParticularWeek(int byWeek);

    @Query(value = "SELECT * FROM meeting WHERE WEEK(start) = WEEK(now() - interval ? week)",nativeQuery = true)
    Optional<List<Meeting>> getAllMeetingForPastParticularWeek(int byWeek);



}

