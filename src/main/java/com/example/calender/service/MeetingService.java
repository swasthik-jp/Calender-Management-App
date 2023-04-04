package com.example.calender.service;

import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;
import com.example.calender.exception.PolicyViolationException;

import java.util.Date;
import java.util.Set;

public interface MeetingService {

    public Boolean canSchedule(Set<String> attendees, Date start, Date end) throws PolicyViolationException;

    public Long scheduleMeeting(Meeting meeting);

    public MeetingStatus changeMeetingStatus(Long id,MeetingStatus newStatus);

    public Meeting getMeetingDetails(Long id);











}
