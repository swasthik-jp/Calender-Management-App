package com.example.calender.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
public class MeetingRoomDto {
    private Long id;
    @NotNull(message = "Name should be provided")
    @NotBlank(message = "Can't be blank")
    private String name;

    @NotNull(message = "Capacity of meeting room not provided")
    private long capacity;

    private boolean isOperational = Boolean.FALSE;

    private OfficeDto office;
}
