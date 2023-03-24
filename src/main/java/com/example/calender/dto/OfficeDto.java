package com.example.calender.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@Builder
//@JsonIgnoreProperties("id")
public class OfficeDto {

    private Long id;

    @JsonProperty("location")
    private String location;
}
