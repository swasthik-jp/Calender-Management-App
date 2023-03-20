package com.example.calender.controller;


import com.example.calender.dto.dtoEmployee;
import com.example.calender.entity.Employee;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.EmployeeServiceImpl;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.logging.Level;
import java.util.stream.Collectors;


@RestController
@Slf4j()
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public EmployeeService employeeServiceImpl;

    public EmployeeController(EmployeeServiceImpl employeeServiceImpl){
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping()
    List<dtoEmployee> getAllEmployees( ){
        log.debug("Received Request to display all Employees");
        return employeeServiceImpl.getAllEmployees().stream()
              .map(employee -> modelMapper.map(employee, dtoEmployee.class))
              .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    ResponseEntity<dtoEmployee> getEmployee(@PathVariable Long id){
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.getEmployeesById(id), dtoEmployee.class), HttpStatus.OK);
    }

    @GetMapping("q")
    ResponseEntity<dtoEmployee> getEmployeeByEmail(@RequestParam(name = "email",required = false) String email){
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.getEmployeesByEmail(email), dtoEmployee.class), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<dtoEmployee> insertEmployee(@RequestBody Employee employee){
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.saveEmployee(employee),dtoEmployee.class),HttpStatus.CREATED);

    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteEmployee(@PathVariable("id") long  id){
        employeeServiceImpl.deleteEmployee(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    ResponseEntity<dtoEmployee> updateEmployee(@RequestBody dtoEmployee dtoemployee,@PathVariable("id") long  id){
        Employee employee = modelMapper.map(dtoemployee, Employee.class);
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.updateEmployee(employee,id), dtoEmployee.class),HttpStatus.OK);

    }


}
