package com.example.calender.controller;

import com.example.calender.entity.Employee;
import com.example.calender.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

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
    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteEmployee(@PathVariable("id") long  id){
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("employee deleted successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee,@PathVariable("id") long  id){
        return new ResponseEntity<>(employeeService.updateEmployee(employee,id),HttpStatus.OK);
    }


}
