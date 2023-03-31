package com.example.calender.controller;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.entity.Employee;
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

import java.util.List;

@CrossOrigin
@RestController
@Slf4j
public class MeetingController {


    @Autowired
    private MeetingService meetingService;
    @Autowired
    private Mapper<Meeting, MeetingDto> meetingDtoMapper;


    @PostMapping("/meeting")
    @ResponseBody
    public ResponseEntity<MeetingDto> scheduleMeeting(@RequestBody MeetingDto meetingDto)
    {
        Meeting meeting = meetingDtoMapper.toEntity(meetingDto);
        System.out.println(meeting.toString());
        return new ResponseEntity<>(meetingDtoMapper.toDto(meetingService.scheduleMeeting(meeting)),HttpStatus.CREATED);
    }

    @GetMapping("/meetings")
    List<MeetingDto> getAllEmployees() {
        return meetingService.getAllMeetings().stream()
                .map(meeting -> meetingDtoMapper.toDto(meeting))
                .toList();
    }


}
