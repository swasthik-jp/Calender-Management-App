package com.example.calender.mapper;

import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MeetingRoomMapper implements Mapper<MeetingRoom, MeetingRoomDto> {

    @Autowired
    private Mapper<Office, OfficeDto> officeDtoMapper;

    @Override
    public MeetingRoomDto toDto(MeetingRoom model) {
        return MeetingRoomDto.builder()
                .id(model.getId())
                .name(model.getName())
                .capacity(model.getCapacity())
                .office(officeDtoMapper.toDto(model.getOffice()))
                .isOperational(model.isOperational())
                .build();
    }

    @Override
    public MeetingRoom toEntity(MeetingRoomDto dto) {
        return MeetingRoom.builder()
                .capacity(dto.getCapacity())
                .isOperational(dto.isOperational())
                .name(dto.getName())
                .office(officeDtoMapper.toEntity(dto.getOffice()))
                .build();
    }
}
