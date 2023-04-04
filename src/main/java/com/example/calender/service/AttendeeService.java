package com.example.calender.service;

import com.example.calender.entity.Attendee;

import java.util.List;

public interface AttendeeService {
    public List<Attendee> getAllAttendees();

    public Attendee save(Attendee attendee);
}
