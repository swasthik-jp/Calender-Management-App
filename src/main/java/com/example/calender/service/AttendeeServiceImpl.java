package com.example.calender.service;

import com.example.calender.entity.Attendee;
import com.example.calender.repository.AttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AttendeeServiceImpl implements AttendeeService{

    @Autowired
    private AttendeeRepository attendeeRepository;
    @Override
    public List<Attendee> getAllAttendees() {
        return attendeeRepository.findAll();
    }

    @Override
    public Attendee save(Attendee attendee) {
        return attendeeRepository.save(attendee);
    }
}
