package com.example.calender.dto;

import com.example.calender.entity.Office;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@Builder
public class EmployeeDto {

    private Long id;

    private String name;

    @JsonAlias("email_id")
    private String email;

    @JsonProperty("address")
    @JsonAlias("home_address")
    private String houseAddress;

    @JsonProperty("ph")
    @JsonAlias({"phone","mobile"})
    private String mob;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date dob;
    private OfficeDto office;
}
