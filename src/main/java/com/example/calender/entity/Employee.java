package com.example.calender.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

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
    private long id;
    private String name;
    private String email;
    private String houseAddress;
    private String mob;
    private Date dob;
    @ManyToOne()
    @JoinColumn(name = "fkOFFICE_ID",nullable = false)
    private Office office;
}
