package com.example.calender.dto;

import com.example.calender.constants.MeetingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Builder
public class MeetingDto {

    private Long id;
    @JsonProperty(value = "host")
    @Email(message = "Email id is invalid")
    private String hostEmail;
    private String agenda;
    private String description;

    @JsonProperty(value = "start")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "IST")
    private Date startTimeStamp;
    @JsonProperty(value = "end")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "IST")
    private Date endTimeStamp;
    private Long allocatedRoomId;
    @NotNull
    private Set<String> attendees;
    private MeetingStatus status = MeetingStatus.PENDING;
}
