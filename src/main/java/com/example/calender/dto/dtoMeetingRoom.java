package com.example.calender.dto;

import com.example.calender.entity.Office;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class dtoMeetingRoom {
    private Long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Capacity")
    private long capacity;

    @JsonProperty("IsOperational")
    private boolean isOperational;

    @JsonProperty("Office")
    private Office office;
}
