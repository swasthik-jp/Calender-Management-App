package com.example.calender.dto;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeDto {
    @NotNull
    private Long meetingId;

    @NotNull
    private Long employeeId;

    @NotNull
    private AttendingStatus isAttending;
}
