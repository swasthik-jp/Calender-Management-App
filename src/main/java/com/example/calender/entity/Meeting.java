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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "host")
    private Employee host;

    private String agenda;
    private String description;

    @Column(name = "start",nullable = false)
    private Date startTimeStamp;
    @Column(name = "end",nullable = false)
    private Date endTimeStamp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allocated_room_id")
    private MeetingRoom allocatedRoom;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "meeting_attendees",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "attendees_id")
    )
    private Set<Attendee> attendees;

    private MeetingStatus status = MeetingStatus.PENDING;



}
