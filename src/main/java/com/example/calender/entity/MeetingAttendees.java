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
@Table(name = "attendees")
@Builder
public class MeetingAttendees {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToMany
    Set<Employee> employee;

    @ManyToMany
    Set<Meeting> meeting;

    @Column(name = "is_attending")
    AttendingStatus isAttending = AttendingStatus.NO;
}
