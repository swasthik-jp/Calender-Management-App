package com.example.calender.service;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Attendee;
import com.example.calender.entity.Meeting;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.PolicyViolationException;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.MeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AttendeeService attendeeService;

    @Autowired
    private MeetingRoomService meetingRoomService;

    public MeetingServiceImpl() {
    }

    @Override
    public Boolean canSchedule(Set<String> attendees, Date start, Date end)
            throws PolicyViolationException {
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        long timeInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Date now = new Date();

        if (attendees.size() > 6 || timeInMinutes < 30)
            throw new PolicyViolationException("A");

        int startHour = Integer.parseInt(start.toString().split(" ")[3].split(":")[0]);
        int endHour = Integer.parseInt(end.toString().split(" ")[3].split(":")[0]);

        if (startHour < 10 || endHour >= 18)
            throw new PolicyViolationException("B");
        if (end.before(start))
            throw new IllegalArgumentException("End Date Cannot be before Start Date!");


        if (start.before(now))
            throw new IllegalArgumentException("Can't schedule meeting in `Past`!");

        Optional<List<Pair<Long, Long>>> allMeetings = meetingRepository.getAllMeetingScheduleForGivenDateRange(start, end);

        if (allMeetings.isPresent()) {
            Set<Long> uniqueRoomsIds = allMeetings.get().stream()
                    .map(Pair::getSecond)
                    .collect(Collectors.toSet());
            List<MeetingRoom> allMeetingRooms = meetingRoomService.getAllMeetingRooms();

            if (uniqueRoomsIds.size() == allMeetingRooms.size()) { // if there is no meeting room available (all are occupied)
                return false;
            }

//            for (Pair<Long, Long> meetId : allMeetings.get()) {
//                Meeting meeting = meetingRepository.findById(meetId.getFirst()).orElseThrow(() -> new ResourceNotFoundException("Meeting", "id", meetId.getFirst()));
//                for (Attendee meetAttendee : meeting.getAttendees()) {
//                    if (meetAttendee.getIsAttending() == AttendingStatus.YES) {
//                        return false;
//                    }
//                }
//            }
            return true;
        } else {
            if (meetingRoomService.getAllMeetingRooms().size() > 0) {
                return true;
            }
            return false;
        }
    }

    @Override
    public Long scheduleMeeting(Meeting meeting) {
        for (Attendee attendee : meeting.getAttendees()) {
            attendee.setIsAttending(AttendingStatus.PENDING);
            attendeeService.save(attendee);
        }
        meeting.setStatus(MeetingStatus.PENDING);
        Meeting m = meetingRepository.save(meeting);
        return null;
    }

    @Override
    public MeetingStatus changeMeetingStatus(Long id, MeetingStatus newStatus) {
        return null;
    }

    @Override
    public Meeting getMeetingDetails(Long id) {
        return null;
    }
}
