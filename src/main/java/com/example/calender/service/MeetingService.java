package com.example.calender.service;

import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;

import java.util.Date;
import java.util.Set;

public interface MeetingService {

    public Set<Long> canSchedule(Set<Long> attendees, Date start, Date end);

    public Long scheduleMeeting(Meeting meeting);

    public MeetingStatus changeMeetingStatus(Long id,MeetingStatus newStatus);










}
