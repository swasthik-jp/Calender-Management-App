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
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMP_ID")
    private Long id;

    private String name;

    private String email;

    private String houseAddress;

    private String mob;

    private Date dob;

    @ManyToOne()
    @JoinColumn(name = "OFFICE_ID",nullable = false)
    private Office office;


}
