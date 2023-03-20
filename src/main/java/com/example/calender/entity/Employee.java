package com.example.calender.entity;

<<<<<<< HEAD
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
=======
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Calendar;
>>>>>>> nv_dto
import java.util.Date;


@Entity
<<<<<<< HEAD
=======
@AllArgsConstructor
@NoArgsConstructor
@Data
>>>>>>> nv_dto
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    private long id;

    private String name;
    private String email;
    private String officeAddress;
    private String houseAddress;
    private String mob;
    private Date dob;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getHouseAddress() {
        return houseAddress;
    }

    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;

    }
=======
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
    @JoinColumn(name = "fkOFFICE_ID",nullable = false)
    private Office office;

>>>>>>> nv_dto

}
