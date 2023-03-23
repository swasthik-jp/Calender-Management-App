package com.example.calender.controller;


import com.example.calender.dto.EmployeeDto;
import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.EmployeeServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class EmployeeController{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public EmployeeService employeeServiceImpl;

    public EmployeeController(EmployeeServiceImpl employeeServiceImpl){
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping("/employees")
    List<EmployeeDto> getAllEmployees(){
        log.debug("received request to display all employees");
        return employeeServiceImpl.getAllEmployees().stream()
              .map(employee -> modelMapper.map(employee, EmployeeDto.class))
              .toList();
    }

    @GetMapping("/employee/{id}")
    @Deprecated
    @SneakyThrows
    ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id){
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.getEmployeeById(id), EmployeeDto.class), HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/employee")
    ResponseEntity<EmployeeDto> getEmployeeByIdOrEmail(@RequestParam(name = "email",required = false) String email, @RequestParam(name = "id",required = false)Long id){
        if(email!=null)
            return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.getEmployeeByEmail(email), EmployeeDto.class), HttpStatus.OK);
        else if (id!=null) {
            return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.getEmployeeById(id), EmployeeDto.class), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @SneakyThrows
    @PostMapping("/employee")
    ResponseEntity<EmployeeDto> insertEmployee(@RequestBody EmployeeDto dtoEmployee){
        dtoEmployee.setId(null);
        if(dtoEmployee.getEmail()!= null && employeeServiceImpl.getEmployeeByEmail(dtoEmployee.getEmail())!=null)
            throw new ResourceAlreadyExistsException("employee","email",dtoEmployee.getEmail());
        Employee employee = modelMapper.map(dtoEmployee, Employee.class);
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.saveEmployee(employee), EmployeeDto.class),HttpStatus.CREATED);

    }

    @DeleteMapping("/employee/{id}")
    ResponseEntity<String> deleteEmployee(@PathVariable("id") long  id){

        try {
            employeeServiceImpl.deleteEmployee(id);
            return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            return new ResponseEntity<>("FAILED: Employee Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/employee/{id}")
    @SneakyThrows
    ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto dtoEmployee, @PathVariable("id") long  id){
        dtoEmployee.setId(null);
        Employee employee = modelMapper.map(dtoEmployee, Employee.class);
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.updateEmployee(employee, id), EmployeeDto.class), HttpStatus.OK);

    }


}
