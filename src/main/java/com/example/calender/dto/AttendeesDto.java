package com.example.calender.dto;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;

import java.util.Set;

public class AttendeesDto {

    private Long id;
    private Employee employee;
    AttendingStatus isAttending = AttendingStatus.PENDING;

}
