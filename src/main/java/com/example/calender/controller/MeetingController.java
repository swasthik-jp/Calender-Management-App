package com.example.calender.controller;

import com.example.calender.dto.MeetingDto;
import com.example.calender.entity.Meeting;
import com.example.calender.mapper.Mapper;
import com.example.calender.mapper.MeetingMapper;
import com.example.calender.service.EmployeeServiceImpl;
import com.example.calender.service.MeetingService;
import com.example.calender.service.MeetingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class MeetingController {


    @Autowired
    MeetingService meetingService;
    @Autowired
    Mapper<Meeting, MeetingDto> meetingMapper;

    public MeetingController(MeetingServiceImpl meetingService) {
        this.meetingService = meetingService;
    }


}
