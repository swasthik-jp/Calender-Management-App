package com.example.calender.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
public class OfficeDto {

    @JsonProperty("Id")
    private Long id;

    @JsonProperty("location")
    private String location;
}
