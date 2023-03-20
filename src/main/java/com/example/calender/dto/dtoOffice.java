package com.example.calender.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class dtoOffice {

    private Long officeID;
    @JsonProperty("officeLocation")
    @JsonAlias({"Location","loc"})
    private String officeLocation;
}
