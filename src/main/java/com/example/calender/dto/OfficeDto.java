package com.example.calender.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
public class OfficeDto {

    @JsonProperty("officeId")
    @JsonAlias("office_id")
    private Long officeID;

    @JsonProperty("officeLocation")
    @JsonAlias("office_location")
    private String officeLocation;
}
