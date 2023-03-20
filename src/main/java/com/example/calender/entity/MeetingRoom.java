package com.example.calender.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MeetingRoom_ID")
    private Long id;

    private String name;

    private long capacity;

    private boolean isOperational;

    @ManyToOne
    @JoinColumn(name = "OFFICE_ID",nullable = false)
    private Office office;

}
