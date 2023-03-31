package com.example.calender.service;

import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface MeetingService {

    public Set<Long> canSchedule(Set<Long> attendees, Date start, Date end);

    public Meeting scheduleMeeting(Meeting meeting);

    public MeetingStatus changeMeetingStatus(Long id,MeetingStatus newStatus);

    public Meeting getMeetingDetails(Long id);


    public List<Meeting> getAllMeetings();
}
