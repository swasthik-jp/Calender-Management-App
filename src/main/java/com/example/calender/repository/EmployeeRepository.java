package com.example.calender.repository;

import com.example.calender.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmail(String email);

    @Query(value = "SELECT * FROM employee WHERE office_id=?",nativeQuery = true)
    Optional<List<Employee>> findAllByOfficeId(Long officeId);


}
