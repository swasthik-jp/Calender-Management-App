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
    @JoinColumn(name = "employee_id",nullable = false)
    private Employee employee;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Meeting meeting;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    AttendingStatus isAttending = AttendingStatus.PENDING;
}
