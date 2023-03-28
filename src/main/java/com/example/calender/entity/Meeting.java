package com.example.calender.entity;

import com.example.calender.constants.MeetingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "meeting")
@Builder
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "host")
    private Employee host;

    private String agenda;
    private String description;

    @Column(name = "start")
    private Date startTimeStamp;
    @Column(name = "end")
    private Date endTimeStamp;


    @OneToOne
    @JoinColumn(name = "allocated_room")
    private MeetingRoom allocatedRoom;

    private MeetingStatus status = MeetingStatus.PENDING;



}
