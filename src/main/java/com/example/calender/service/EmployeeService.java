package com.example.calender.service;

import com.example.calender.entity.Employee;

import java.util.List;

public interface EmployeeService {

    public Employee saveEmployee(Employee employee);
    public List<Employee> getAllEmployees();
    public Employee getEmployeesById(long id);
    public Employee getEmployeesByEmail(String email);
    public Employee updateEmployee(Employee employee, long id);
    public void deleteEmployee(long id);

}
