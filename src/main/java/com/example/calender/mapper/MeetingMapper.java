package com.example.calender.mapper;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.MeetingDto;
import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

public class MeetingMapper implements Mapper<Meeting, MeetingDto> {

    @Autowired
    private Mapper<Employee, EmployeeDto> employeeDtoMapper;
    @Autowired
    private Mapper<MeetingRoom, MeetingRoomDto> meetingRoomDtoMapper;
    @Override
    public MeetingDto toDto(Meeting model) {
        return MeetingDto.builder()
                .id(model.getId())
                .agenda(model.getAgenda())
                .description(model.getDescription())
                .startTimeStamp(model.getStartTimeStamp())
                .endTimeStamp(model.getEndTimeStamp())
                .allocatedRoom(meetingRoomDtoMapper.toDto(model.getAllocatedRoom()))
                .build();
    }

    @Override
    public Meeting toEntity(MeetingDto dto) {
        return Meeting.builder()
                .id(dto.getId())
                .agenda(dto.getAgenda())
                .description(dto.getDescription())
                .startTimeStamp(dto.getStartTimeStamp())
                .endTimeStamp(dto.getEndTimeStamp())
                .allocatedRoom(meetingRoomDtoMapper.toEntity(dto.getAllocatedRoom()))
                .build();
    }
}
