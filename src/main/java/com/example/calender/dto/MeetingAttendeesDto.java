package com.example.calender.dto;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Meeting;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

public class MeetingAttendeesDto {

    Employee employee;
    Meeting meeting;
    AttendingStatus isAttending = AttendingStatus.NO;
}
