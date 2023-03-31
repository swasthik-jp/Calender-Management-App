package com.example.calender.repository;

import com.example.calender.entity.Attendee;
import com.example.calender.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
}
