package com.example.calender.dao;

import com.example.calender.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeDao extends JpaRepository<Employee,Long> {
}
