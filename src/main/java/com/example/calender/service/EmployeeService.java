package com.example.calender.service;


import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;

import java.util.List;

public interface EmployeeService {

    Employee saveEmployee(Employee employee) throws ResourceAlreadyExistsException;

    List<Employee> getAllEmployees();

    Employee getEmployeeById(long id) throws ResourceNotFoundException;

    Employee getEmployeeByEmail(String email) throws ResourceNotFoundException;

    Employee updateEmployee(Employee employee, long id) throws ResourceNotFoundException;

    void deleteEmployeeById(long id) throws ResourceNotFoundException;

    void deleteEmployeeByEmail(String email) throws ResourceNotFoundException;

    boolean checkEmptyOffice(Long fkOfficeId) throws ResourceNotFoundException;

}
