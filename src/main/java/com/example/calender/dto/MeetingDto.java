package com.example.calender.dto;

import com.example.calender.constants.MeetingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Builder
public class MeetingDto {

    private Long id;

    private String agenda;
    private String description;

    @JsonProperty(value = "start")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private Date startTimeStamp;
    @JsonProperty(value = "end")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private Date endTimeStamp;

    MeetingRoomDto allocatedRoom;
    Set<AttendeesDto> attendees;
    private MeetingStatus status = MeetingStatus.PENDING;
}
