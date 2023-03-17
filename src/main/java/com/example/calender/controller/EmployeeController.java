package com.example.calender.controller;

import com.example.calender.entity.Employee;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.EmployeeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeServiceImpl;

    public EmployeeController(EmployeeServiceImpl employeeServiceImpl){
        this.employeeServiceImpl = employeeServiceImpl;
    }
    @GetMapping()
    List<Employee> getAllEmployees( ){
      return employeeServiceImpl.getAllEmployees();
    }

//    @GetMapping("{id}")
//    ResponseEntity<Employee> getEmployee(@PathVariable("id") long  id){
//      return new ResponseEntity<>(employeeServiceImpl.getEmployeesById(id), HttpStatus.OK);
//    }
    @GetMapping("{id}")
    ResponseEntity<Employee> getEmployee(@PathVariable Long id){
        return new ResponseEntity<>(employeeServiceImpl.getEmployeesById(id), HttpStatus.OK);
    }

    @GetMapping("q")
    ResponseEntity<Employee> getEmployeeByEmail(@RequestParam(name = "email",required = false) String email){
        return new ResponseEntity<>(employeeServiceImpl.getEmployeesByEmail(email), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<Employee> insertEmployee(@RequestBody Employee employee){
        return new ResponseEntity<>(employeeServiceImpl.saveEmployee(employee),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteEmployee(@PathVariable("id") long  id){
        employeeServiceImpl.deleteEmployee(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }

    @PutMapping("{id}")
    ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee,@PathVariable("id") long  id){
        return new ResponseEntity<>(employeeServiceImpl.updateEmployee(employee,id),HttpStatus.OK);
    }


}
