package com.example.calender.service;

import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Meeting;
import com.example.calender.repository.MeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    MeetingRepository meetingRepository;
    @Override
    public Set<Long> canSchedule(Set<Long> attendees, Date start, Date end) {

        return null;
    }

    @Override
    public Meeting scheduleMeeting(Meeting meeting) {

        return meetingRepository.save(meeting);
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
