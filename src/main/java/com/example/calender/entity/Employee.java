package com.example.calender.entity;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Calendar;

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

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Email")
    @JsonAlias("emailId")
    private String email;

    @JsonProperty("Address")
    @JsonAlias("homeAddress")
    private String houseAddress;

    @JsonProperty("Ph")
    @JsonAlias({"Phone","Mobile"})
    private String mob;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonProperty("dob")
    @JsonAlias("BirthDate")
    private Date dob;
    @JsonProperty("Office")
    @ManyToOne()
    @JoinColumn(name = "OFFICE_ID",nullable = false)
    private Office office;


}
