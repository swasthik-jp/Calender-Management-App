package com.example.calender.dao;

import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeDao extends JpaRepository<Office,Long> {
}
