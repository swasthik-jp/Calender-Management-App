package com.example.calender.dto;

import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Employee;
import com.fasterxml.jackson.annotation.JsonAlias;
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
    @JsonProperty(value = "host")
    private String hostEmail;
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

    private Long allocatedRoomId;
    private Set<String> attendees;
    private MeetingStatus status = MeetingStatus.PENDING;
}
