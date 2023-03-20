package com.example.calender.dto;

import com.example.calender.entity.Office;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class dtoEmployee {


    private Long id;

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
    private Office office;
}
