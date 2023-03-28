package com.example.calender.entity;

import com.example.calender.constants.AttendingStatus;

import javax.persistence.*;

public class MeetingAttendees {

    @ManyToMany
    @JoinColumn(name = "employee_id")
    Employee employee;

    @ManyToMany
    @JoinColumn(name = "meeting_id")
    Meeting meeting;

    @Column(name = "is_attending")
    AttendingStatus isAttending = AttendingStatus.NO;
}
