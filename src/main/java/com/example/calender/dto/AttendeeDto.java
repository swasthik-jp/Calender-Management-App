package com.example.calender.dto;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeDto {
    private Long meetingId;
    private Long employeeId;
    private AttendingStatus isAttending;
}
