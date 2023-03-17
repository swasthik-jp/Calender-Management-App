package com.example.calender.dao;

import com.example.calender.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeDao extends JpaRepository<Office,Long> {
}
