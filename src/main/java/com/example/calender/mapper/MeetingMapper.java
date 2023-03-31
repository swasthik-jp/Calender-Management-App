package com.example.calender.mapper;

import com.example.calender.ServiceHelper;
import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MeetingMapper implements Mapper<Meeting, MeetingDto> {

    @Autowired
    private Mapper<Employee, EmployeeDto> employeeDtoMapper;
    @Autowired
    private Mapper<MeetingRoom, MeetingRoomDto> meetingRoomDtoMapper;

    @Autowired
    private ServiceHelper serviceHelper;
    @Override
    public MeetingDto toDto(Meeting model) {
        return MeetingDto.builder()
                .id(model.getId())
                .host(model.getHost().getId())
                .agenda(model.getAgenda())
                .description(model.getDescription())
                .startTimeStamp(model.getStartTimeStamp())
                .endTimeStamp(model.getEndTimeStamp())
                .allocatedRoom(model.getAllocatedRoom().getId())
                .attendees(serviceHelper.employeeToJustEmails(model.getAttendees()))
                .status(model.getStatus())
                .build();
    }

    @Override
    public Meeting toEntity(MeetingDto dto) {
        try {
            System.out.println(dto.toString());
            return Meeting.builder()
                    .host(serviceHelper.getEmployeeById(dto.getHost()))
                    .agenda(dto.getAgenda())
                    .description(dto.getDescription())
                    .startTimeStamp(dto.getStartTimeStamp())
                    .endTimeStamp(dto.getEndTimeStamp())
                    .allocatedRoom(serviceHelper.getMeetingRoomById(dto.getAllocatedRoom()))
                    .status(dto.getStatus())
                    .attendees(serviceHelper.getAllAttendees(dto.getAttendees()))
                    .build();

        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
