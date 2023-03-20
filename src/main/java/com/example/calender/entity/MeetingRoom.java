package com.example.calender.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MeetingRoom_ID")
    private Long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Capacity")
    private long capacity;

    @JsonProperty("IsOperational")
    private boolean isOperational;

    @JsonProperty("Office")
    @ManyToOne
    @JoinColumn(name = "OFFICE_ID",nullable = false)
    private Office office;

}
