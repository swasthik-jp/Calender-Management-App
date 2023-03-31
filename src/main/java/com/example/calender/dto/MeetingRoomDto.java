package com.example.calender.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
public class MeetingRoomDto {
    private Long id;
    @NotNull(message = "Room can't be empty")
    @NotBlank(message = "Room Name is invalid")
    private String name;

    @NotNull(message = "Capacity can't be empty")
    private long capacity;

    private boolean isOperational = Boolean.FALSE;

    private OfficeDto office;
}
