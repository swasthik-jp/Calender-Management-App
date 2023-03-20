package com.example.calender.dto;

import com.example.calender.entity.Office;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class dtoEmployee {

    private long id;
    private String name;
    private String email;
    private String houseAddress;
    private String mob;
    private Date dob;
    private Office office;
}
