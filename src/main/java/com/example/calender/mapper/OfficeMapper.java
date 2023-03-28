package com.example.calender.mapper;

import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Office;
import org.springframework.stereotype.Component;

@Component
public class OfficeMapper implements Mapper<Office, OfficeDto> {
    @Override
    public OfficeDto toDto(Office model) {
        return OfficeDto.builder()
                .id(model.getId())
                .location(model.getLocation())
                .build();
    }

    @Override
    public Office toEntity(OfficeDto dto) {
        return Office.builder()
                .id(dto.getId())
                .location(dto.getLocation())
                .build();
    }
}
