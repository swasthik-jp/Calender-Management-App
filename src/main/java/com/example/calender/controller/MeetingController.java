package com.example.calender.controller;

import com.example.calender.dto.MeetingDto;
import com.example.calender.entity.Meeting;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.mapper.Mapper;
import com.example.calender.mapper.MeetingMapper;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.EmployeeServiceImpl;
import com.example.calender.service.MeetingService;
import com.example.calender.service.MeetingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Slf4j
public class MeetingController {


    @Autowired
    private MeetingService meetingService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private Mapper<Meeting, MeetingDto> meetingMapper;

    public MeetingController(MeetingServiceImpl meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/meeting/{id}")
    ResponseEntity<MeetingDto> getMeetingDetails(@PathVariable Long id)
    {
        return new ResponseEntity<>(meetingMapper.toDto(
                meetingService.getMeetingDetails(id)
        ), HttpStatus.OK);
    }

    @PostMapping("/meeting")
    ResponseEntity<Long> scheduleMeeting(@RequestBody MeetingDto meetingDto) throws ResourceNotFoundException {
        Meeting meeting = meetingMapper.toEntity(meetingDto);
        return new ResponseEntity<>(meetingMapper.toDto(meetingService.scheduleMeeting(meeting)).getId(),HttpStatus.OK);
    }

//    @PostMapping("/meeting")
//    ResponseEntity<MeetingDto> scheduleMeeting(@RequestBody ? )
//    {
//
//    }





}
