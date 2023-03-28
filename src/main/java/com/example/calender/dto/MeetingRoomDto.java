package com.example.calender.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
public class MeetingRoomDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private long capacity;

    private boolean isOperational = Boolean.FALSE;

    private OfficeDto office;
}
