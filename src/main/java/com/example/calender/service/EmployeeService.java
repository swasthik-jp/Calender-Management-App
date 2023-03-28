package com.example.calender.service;


import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;

import java.util.List;

public interface EmployeeService {

    public Employee saveEmployee(Employee employee) throws ResourceAlreadyExistsException;
    public List<Employee> getAllEmployees();
    public Employee getEmployeeById(long id) throws ResourceNotFoundException;
    public Employee getEmployeeByEmail(String email) throws ResourceNotFoundException;
    public Employee updateEmployee(Employee employee, long id) throws ResourceNotFoundException;
    public void deleteEmployee(long id) throws ResourceNotFoundException;

    public boolean checkEmptyOffice(Long fkOfficeId) throws ResourceNotFoundException;

}
