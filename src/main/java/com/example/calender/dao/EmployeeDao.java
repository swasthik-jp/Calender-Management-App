package com.example.calender.dao;

import com.example.calender.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

public interface EmployeeDao extends JpaRepository<Employee,Long> {
=======
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmail(String email);


>>>>>>> nv_dto
}
