package com.example.calender.service;

import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Meeting;

import java.util.Date;
import java.util.Set;

public class MeetingServiceImpl implements MeetingService {
    @Override
    public Set<Long> canSchedule(Set<Long> attendees, Date start, Date end) {
        return null;
    }

    @Override
    public Long scheduleMeeting(Meeting meeting) {
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
