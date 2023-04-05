package com.example.calender.controller;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.dto.AttendeeDto;
import com.example.calender.dto.CanScheduleDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.entity.Meeting;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.MeetingService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
        return new ResponseEntity<>(id.toString(), HttpStatus.CREATED);
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

    @GetMapping("/meeting")
    ResponseEntity meetingForEmployee(@RequestParam(name = "id") Long id,
                                      @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST") @RequestParam(name = "start", required = false) Date start,
                                      @JsonFormat(pattern = "yyyy-MM-dd", timezone = "IST") @RequestParam(name = "end", required = false) Date end,
                                      @RequestParam(name = "filter", required = false) String filter
    ) {
        List<Meeting> meetingDuringThoseDates;
        if (start != null && end != null) {
            meetingDuringThoseDates = meetingService.getMeetingsInCustomRange(start, end);
        } else if (filter != null) {
            switch (filter) {
                case "last_week": {
                    meetingDuringThoseDates = meetingService.getMeetingsInParticularWeek('-', 1);
                    break;
                }
                case "current_week": {
                    meetingDuringThoseDates = meetingService.getMeetingsInParticularWeek('-', 0);
                    break;
                }
                case "next_week": {
                    meetingDuringThoseDates = meetingService.getMeetingsInParticularWeek('+', 1);
                    break;
                }
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Kolkata"));
            LocalDateTime todayMid = LocalDateTime.of(today, midnight);
            Date todayMidnight = Date.from(todayMid.atZone(ZoneId.of("Asia/Kolkata")).toInstant());
            Date tomorrowMidnight = Date.from(todayMid.plusDays(1).atZone(ZoneId.of("Asia/Kolkata")).toInstant());
            meetingDuringThoseDates = meetingService.getMeetingsInCustomRange(todayMidnight, tomorrowMidnight);
        }

        List<MeetingDto> employeeMeeting = meetingService.getParticularEmployeeMeetings(meetingDuringThoseDates, id).stream()
                .map(meeting -> meetingMapper.toDto(meeting))
                .toList();

        return new ResponseEntity<List<MeetingDto>>(employeeMeeting, HttpStatus.OK);
    }

}
