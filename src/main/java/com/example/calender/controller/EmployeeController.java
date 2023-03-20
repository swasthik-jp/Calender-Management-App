package com.example.calender.controller;

<<<<<<< HEAD
import com.example.calender.entity.Employee;
import com.example.calender.service.EmployeeService;
=======
import com.example.calender.dto.dtoEmployee;
import com.example.calender.entity.Employee;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.EmployeeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> nv_dto
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
<<<<<<< HEAD
=======
import java.util.stream.Collectors;
>>>>>>> nv_dto

@RestController
@RequestMapping("/employee")
public class EmployeeController {

<<<<<<< HEAD
    public EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }
    @GetMapping()
    List<Employee> getAllEmployees( ){
      return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    ResponseEntity<Employee> getEmployee(@PathVariable("id") long  id){
      return new ResponseEntity<>(employeeService.getEmployeesById(id), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<Employee> insertEmployee(@RequestBody Employee employee){
        return new ResponseEntity<>(employeeService.saveEmployee(employee),HttpStatus.CREATED);
=======
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public EmployeeService employeeServiceImpl;

    public EmployeeController(EmployeeServiceImpl employeeServiceImpl){
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping()
    List<dtoEmployee> getAllEmployees( ){
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
>>>>>>> nv_dto
    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteEmployee(@PathVariable("id") long  id){
<<<<<<< HEAD
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("employee deleted successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee,@PathVariable("id") long  id){
        return new ResponseEntity<>(employeeService.updateEmployee(employee,id),HttpStatus.OK);
=======
        employeeServiceImpl.deleteEmployee(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    ResponseEntity<dtoEmployee> updateEmployee(@RequestBody dtoEmployee dtoemployee,@PathVariable("id") long  id){
        Employee employee = modelMapper.map(dtoemployee, Employee.class);
        return new ResponseEntity<>(modelMapper.map(employeeServiceImpl.updateEmployee(employee,id), dtoEmployee.class),HttpStatus.OK);
>>>>>>> nv_dto
    }


}
