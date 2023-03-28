package com.example.calender.mapper;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements Mapper<Employee, EmployeeDto> {

    @Autowired
    private Mapper<Office, OfficeDto> officeDtoMapper;

    @Override
    public EmployeeDto toDto(Employee model) {
        return EmployeeDto.builder()
                .id(model.getId())
                .email(model.getEmail())
                .name(model.getName())
                .dob(model.getDob())
                .mob(model.getMob())
                .houseAddress(model.getHouseAddress())
                .office(officeDtoMapper.toDto(model.getOffice()))
                .build();
    }

    @Override
    public Employee toEntity(EmployeeDto dto) {
        return Employee.builder()
                .mob(dto.getMob())
                .email(dto.getEmail())
                .name(dto.getName())
                .dob(dto.getDob())
                .houseAddress(dto.getHouseAddress())
                .office(officeDtoMapper.toEntity(dto.getOffice()))
                .build();
    }
}
