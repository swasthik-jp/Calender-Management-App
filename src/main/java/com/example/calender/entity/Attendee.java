package com.example.calender.entity;

import com.example.calender.constants.AttendingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "attendee")
@Builder
public class Attendee {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id",nullable = false)
    private Employee employee;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "attendees")
    private Set<Meeting> meetings;

    @Column(name = "status",nullable = false)
    AttendingStatus isAttending = AttendingStatus.PENDING;
}
