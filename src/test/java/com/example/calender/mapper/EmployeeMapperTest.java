package com.example.calender.mapper;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeMapperTest {

    @Mock
    Mapper<Office, OfficeDto> officeDToMapper;

    @InjectMocks
    EmployeeMapper employeeMapper;


    @Test
    public void toDto_WhenValidEmployee_ThenParametersMatch() {
        Office office=Office.builder()
                .id(2L)
                .location("new york")
                .build();
        Employee employee=Employee.builder()
                .id(1L)
                .name("emp1")
                .email("emp@mail.com")
                .houseAddress("houseAddress")
                .mob("9876543211")
                .dob(new Date(2023,1,1))
                .office(office).
                build();

        OfficeDto officeDto=OfficeDto.builder()
                .id(2L)
                .location(office.getLocation())
                .build();


       when(officeDToMapper.toDto(office)) .thenReturn(officeDto);
        EmployeeDto employeeDto= employeeMapper.toDto(employee);
        assertEquals(employee.getId(),employeeDto.getId());
        assertEquals(employee.getName(),employeeDto.getName());
        assertEquals(office.getId(),employeeDto.getOffice().getId());
    }

    @Test
   public void toEntity_WhenValidEmployeeDtoWithNullID_ThenParametersMatch() {
        Office office=Office.builder()
                .location("new york")
                .build();

        OfficeDto officeDto=OfficeDto.builder()
                .location(office.getLocation())
                .build();

        EmployeeDto employeeDto=EmployeeDto.builder()
                .name("emp1")
                .email("emp@mail.com")
                .houseAddress("houseAddress")
                .mob("9876543211")
                .dob(new Date(2023,1,1))
                .office(officeDto).
                build();

        when(officeDToMapper.toEntity(officeDto)) .thenReturn(office);
        Employee employee= employeeMapper.toEntity(employeeDto);
        assertEquals(null,employee.getId());
        assertEquals(employeeDto.getName(),employee.getName());
        assertEquals(null,employee.getOffice().getId());
    }

    @Test
    public void toEntity_WhenValidEmployeeDtoWithValidID_ThenIDIsNull() {
        Office office=Office.builder()
                .location("new york")
                .build();

        OfficeDto officeDto=OfficeDto.builder()
                .location(office.getLocation())
                .build();

        EmployeeDto employeeDto=EmployeeDto.builder()
                .id(1L)
                .name("emp1")
                .email("emp@mail.com")
                .houseAddress("houseAddress")
                .mob("9876543211")
                .dob(new Date(2023,1,1))
                .office(officeDto).
                build();

        when(officeDToMapper.toEntity(officeDto)) .thenReturn(office);
        Employee employee= employeeMapper.toEntity(employeeDto);
        assertEquals(null,employee.getId());
    }
}