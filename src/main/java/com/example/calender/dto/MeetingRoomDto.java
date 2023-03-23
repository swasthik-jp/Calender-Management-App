package com.example.calender.dto;

import com.example.calender.entity.Office;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MeetingRoomDto {
    private Long id;

    private String name;

    private long capacity;

    private boolean isOperational;

    private OfficeDto office;
}
