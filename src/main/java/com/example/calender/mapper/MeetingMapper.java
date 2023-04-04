package com.example.calender.mapper;

import com.example.calender.dto.MeetingDto;
import com.example.calender.entity.Attendee;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MeetingMapper implements Mapper<Meeting, MeetingDto> {
    @Autowired
    MeetingRoomService meetingRoomService;

    @Autowired
    EmployeeService employeeService;


    @Override
    public MeetingDto toDto(Meeting model) {
        Set<String> attendies = model.getAttendees().stream()
                .map(attendee -> attendee.getEmployee().getEmail())
                .collect(Collectors.toSet());
        return MeetingDto.builder()
                .id(model.getId())
                .agenda(model.getAgenda())
                .description(model.getDescription())
                .startTimeStamp(model.getStartTimeStamp())
                .endTimeStamp(model.getEndTimeStamp())
                .allocatedRoomId(model.getAllocatedRoom().getId())
                .attendees(attendies)
                .hostEmail(model.getHost().getEmail())
                .status(model.getStatus())
                .build();
    }

    @Override
    public Meeting toEntity(MeetingDto dto) throws ResourceNotFoundException {
        MeetingRoom meetingRoom = null;
        if (dto.getAllocatedRoomId() != null) {
            meetingRoom = meetingRoomService.getMeetingRoomById(dto.getAllocatedRoomId());
        }
        Set<Attendee> employees = dto.getAttendees().stream()
                .map(email -> Attendee.builder()
                        .employee(employeeService.getEmployeeByEmail(email))
                        .build())
                .collect(Collectors.toSet());
        Employee host = employeeService.getEmployeeByEmail(dto.getHostEmail());
        return Meeting.builder()
                .agenda(dto.getAgenda())
                .description(dto.getDescription())
                .startTimeStamp(dto.getStartTimeStamp())
                .endTimeStamp(dto.getEndTimeStamp())
                .allocatedRoom(meetingRoom)
                .attendees(employees)
                .host(host)
                .build();
    }
}
