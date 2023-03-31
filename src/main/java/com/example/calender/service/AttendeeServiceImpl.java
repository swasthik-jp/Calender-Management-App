package com.example.calender.service;

import com.example.calender.entity.Attendee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.repository.AttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AttendeeServiceImpl implements AttendeeService{

    @Autowired
    AttendeeRepository attendeeRepository;
    @Override
    public Attendee saveAttendee(Attendee attendee) throws ResourceAlreadyExistsException {
        return attendeeRepository.save(attendee);
    }

    @Override
    public List<Attendee> getAllAttendee() {    return attendeeRepository.findAll();    }
}
