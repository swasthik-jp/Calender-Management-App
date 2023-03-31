package com.example.calender.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@Builder
public class EmployeeDto {

    private Long id;

    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name can be empty")
    private String name;

    @NotNull(message = "Email can't be null")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",message = "Email id is invalid")
    @JsonAlias("email_id")
    private String email;

    @JsonProperty("address")
    @JsonAlias("home_address")
    private String houseAddress;

    @JsonProperty("ph")
    @JsonAlias({"phone", "mobile"})
    private String mob;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;

    @NotNull(message = "Office Id missing")
    private OfficeDto office;
}
