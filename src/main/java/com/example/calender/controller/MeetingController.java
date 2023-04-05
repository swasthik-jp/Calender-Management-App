package com.example.calender.controller;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.dto.AttendeeDto;
import com.example.calender.dto.CanScheduleDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.entity.Meeting;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.MeetingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin
@RestController
@Slf4j
public class MeetingController {


    @Autowired
    MeetingService meetingService;
    @Autowired
    Mapper<Meeting, MeetingDto> meetingMapper;

    @GetMapping("/meeting/{id}")
    ResponseEntity<MeetingDto> getMeetingDetails(@NotNull @PathVariable Long id) {
        return new ResponseEntity<>(meetingMapper.toDto(
                meetingService.getMeetingDetails(id)
        ), HttpStatus.OK);
    }

    @PostMapping("/meeting")
    ResponseEntity<String> scheduleMeeting(@RequestBody MeetingDto meetingDto) {
        Meeting requestMeeting = meetingMapper.toEntity(meetingDto);
        Long id = meetingService.scheduleMeeting(requestMeeting);
        if (id == null)
            return new ResponseEntity<>("Can't schedule Meeting", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        return new ResponseEntity<>(id.toString(), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/can-schedule")
    ResponseEntity<Boolean> canSchedule(@Valid @RequestBody CanScheduleDto scheduleDto) {
        Boolean canSchedule = meetingService.canSchedule(scheduleDto.getAttendees(), scheduleDto.getStart(), scheduleDto.getEnd());
        return new ResponseEntity<>(canSchedule, HttpStatus.OK);
    }


    @PutMapping("/cancel-meeting/{id}")
    ResponseEntity<String> cancelMeeting(@NotNull @PathVariable Long id) {
        meetingService.changeMeetingStatus(id, MeetingStatus.CANCELLED);
        return new ResponseEntity<>("Meeting Cancelled Successfully", HttpStatus.OK);
    }

    @PutMapping("/attendee-status")
    ResponseEntity<String> setAttendeeStatus(@RequestBody AttendeeDto attendee) {
        AttendingStatus status = meetingService.setAttendeeStatus(attendee.getMeetingId(), attendee.getEmployeeId(), attendee.getIsAttending());
        return new ResponseEntity<>("Status successfully changed to " + status, HttpStatus.OK);
    }
}
