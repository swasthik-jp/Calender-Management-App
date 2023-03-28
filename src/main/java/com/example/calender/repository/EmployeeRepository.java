package com.example.calender.repository;

import com.example.calender.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    @Query(value = "SELECT * FROM employee WHERE is_deleted = false AND office_id=?",nativeQuery = true)
    Optional<List<Employee>> findAllByOfficeId(Long officeId);

}
