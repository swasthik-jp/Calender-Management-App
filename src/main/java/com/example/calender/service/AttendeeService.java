package com.example.calender.service;

import com.example.calender.entity.Attendee;
import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;

import java.util.List;

public interface AttendeeService {


    public Attendee saveAttendee(Attendee attendee) throws ResourceAlreadyExistsException;

    List<Attendee> getAllAttendee();




}
