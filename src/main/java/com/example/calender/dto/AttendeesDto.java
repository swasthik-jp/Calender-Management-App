package com.example.calender.dto;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.entity.Employee;

public class AttendeesDto {

    private Long id;
    private Employee employee;
    private AttendingStatus isAttending = AttendingStatus.PENDING;

}
