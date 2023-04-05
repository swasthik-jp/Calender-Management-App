package com.example.calender.service;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Meeting;
import com.example.calender.exception.PolicyViolationException;
import com.example.calender.exception.ResourceNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeetingService {

    Boolean canSchedule(Set<String> attendees, Date start, Date end) throws PolicyViolationException;

    Long scheduleMeeting(Meeting meeting);

    MeetingStatus changeMeetingStatus(Long id, MeetingStatus newStatus);

    Meeting getMeetingDetails(Long id);

    AttendingStatus setAttendeeStatus(Long meetingId, Long employeeId, AttendingStatus attendingStatus);

    public List<Meeting> getMeetingsInCustomRange(Date start, Date end);

    public List<Meeting> getMeetingsInParticularWeek(char sign,int byWeek);

    public List<Meeting> getParticularEmployeeMeetings(List<Meeting> meetingsList, Long id);


}
