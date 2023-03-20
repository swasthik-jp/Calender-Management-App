package com.example.calender.entity;

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
    private long id;
    private String name;
    private long capacity;
    private boolean isOperational;
    @ManyToOne
    @JoinColumn(name = "fkOFFICE_ID",nullable = false)
    private Office office;

}
