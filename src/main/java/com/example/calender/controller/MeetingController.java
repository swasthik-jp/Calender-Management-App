package com.example.calender.controller;

import com.example.calender.dto.MeetingDto;
import com.example.calender.entity.Meeting;
import com.example.calender.mapper.Mapper;
import com.example.calender.mapper.MeetingMapper;
import com.example.calender.service.EmployeeServiceImpl;
import com.example.calender.service.MeetingService;
import com.example.calender.service.MeetingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin

@Slf4j
public class MeetingController {


    @Autowired
    MeetingService meetingService;
    @Autowired
    Mapper<Meeting, MeetingDto> meetingMapper;

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

//    @PostMapping("/meeting")
//    ResponseEntity<MeetingDto> scheduleMeeting(@RequestBody ? )
//    {
//
//    }





}