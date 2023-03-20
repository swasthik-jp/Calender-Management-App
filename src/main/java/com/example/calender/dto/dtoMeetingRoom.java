package com.example.calender.dto;

import com.example.calender.entity.Office;
import lombok.Data;

@Data
public class dtoMeetingRoom {
    private long id;
    private String name;
    private long capacity;
    private boolean isOperational;
    private Office office;
}
