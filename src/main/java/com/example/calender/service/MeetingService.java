package com.example.calender.service;

import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Meeting;
import com.example.calender.exception.PolicyViolationException;
import org.aspectj.apache.bcel.classfile.Module;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MeetingService {

    public Boolean canSchedule(Set<String> attendees, Date start, Date end) throws PolicyViolationException;

    public Long scheduleMeeting(Meeting meeting);

    public MeetingStatus changeMeetingStatus(Long id, MeetingStatus newStatus);

    public Meeting getMeetingDetails(Long id);

    public List<Meeting> getMeetingsInCustomRange(Date start, Date end);

    public List<Meeting> getMeetingsInParticularWeek(char sign,int byWeek);

    public List<Meeting> getParticularEmployeeMeetings(List<Meeting> meetingsList, Long id);


}
