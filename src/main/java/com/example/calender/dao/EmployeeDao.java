package com.example.calender.dao;

import com.example.calender.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmail(String email);
}
