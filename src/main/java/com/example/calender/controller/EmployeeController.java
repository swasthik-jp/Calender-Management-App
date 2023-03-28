package com.example.calender.controller;


import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.EmployeeServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class EmployeeController {

    @Autowired
    private Mapper<Employee, EmployeeDto> employeeDtoMapper;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RestTemplate restTemplate;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    List<EmployeeDto> getAllEmployees() {
        log.debug("received request to display all employees");
        return employeeService.getAllEmployees().stream()
                .map(employee -> employeeDtoMapper.toDto(employee))
                .toList();
    }

    @GetMapping("/employee/{id}")
    @Deprecated(since = "0.0.14",forRemoval = false)
    @SneakyThrows
    ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(employeeDtoMapper.toDto(employeeService.getEmployeeById(id)), HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/employee")
    ResponseEntity<EmployeeDto> getEmployeeByIdOrEmail(@RequestParam(name = "email", required = false) String email, @RequestParam(name = "id", required = false) Long id) {
        if (email != null)
            return new ResponseEntity<>(employeeDtoMapper.toDto(employeeService.getEmployeeByEmail(email)), HttpStatus.OK);
        else if (id != null) {
            return new ResponseEntity<>(employeeDtoMapper.toDto(employeeService.getEmployeeById(id)), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @SneakyThrows
    @PostMapping("/employee")
    ResponseEntity<EmployeeDto> insertEmployee(@Valid @RequestBody EmployeeDto dtoEmployee) {
        try { //check if the office exists or soft deleted
            restTemplate.getForObject(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/office/" + dtoEmployee.getOffice().getId(), OfficeDto.class);
        } catch (HttpClientErrorException e) { // return BAD REQUEST if office is soft deleted
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Employee employee = employeeDtoMapper.toEntity(dtoEmployee);
        return new ResponseEntity<>(employeeDtoMapper.toDto(employeeService.saveEmployee(employee)), HttpStatus.CREATED);

    }

    @SneakyThrows
    @DeleteMapping("/employee/{id}")
    ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/employee/{id}")
    @SneakyThrows
    ResponseEntity<EmployeeDto> updateEmployee(@Valid @RequestBody EmployeeDto dtoEmployee, @PathVariable("id") long id) {
        Employee employee = employeeDtoMapper.toEntity(dtoEmployee);
        return new ResponseEntity<>(employeeDtoMapper.toDto(employeeService.updateEmployee(employee, id)), HttpStatus.OK);

    }

}
