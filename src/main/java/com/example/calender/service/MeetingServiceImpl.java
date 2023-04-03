package com.example.calender.service;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Attendee;
import com.example.calender.entity.Meeting;
import com.example.calender.repository.AttendeeRepository;
import com.example.calender.repository.MeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Override
    public Set<Long> canSchedule(Set<Long> attendees, Date start, Date end) {
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        long timeInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
//        if(attendees.size() > 6 || timeInMinutes < 30)
//            throw new PolicyViolationException();
            return null;
    }

    @Override
    public Long scheduleMeeting(Meeting meeting) {
        log.debug(meeting.toString());
        for(Attendee attendee : meeting.getAttendees()) {
            attendee.setIsAttending(AttendingStatus.PENDING);
            attendeeRepository.save(attendee);
        }
        meeting.setStatus(MeetingStatus.PENDING);
        Meeting m = meetingRepository.save(meeting);
        log.debug(m.toString());
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
