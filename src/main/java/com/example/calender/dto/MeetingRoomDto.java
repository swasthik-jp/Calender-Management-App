package com.example.calender.dto;

import com.example.calender.entity.Office;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class MeetingRoomDto {
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("capacity")
    private long capacity;

    @JsonProperty("isOperational")
    private boolean isOperational;

    @JsonProperty("office")
    private OfficeDto office;
}
