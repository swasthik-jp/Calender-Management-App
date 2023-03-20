package com.example.calender.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OFFICE_ID")
    private long officeID;
    @Column(name = "OFFICE_LOC")
    @JsonProperty("officeLocation")
    @JsonAlias({"Location","loc"})
    private String officeLocation;

}