package com.example.calender.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficeDto {

    private Long id;

    @NotNull(message = "Office Location must be provided")
    @NotBlank(message = "Location Can't be Empty")
    @JsonProperty("location")
    private String location;
}
