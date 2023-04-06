package com.example.calender.repository;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;


    public Employee initEmployee(){
        Office office=Office.builder()
                .id(1L)
                .build();
        Employee testEmp = Employee.builder()
                .id(10L)
                .email("mail@email.com")
                .office(office)
                .build();
        return testEmp;
    }

    public EmployeeDto initEmployeeDto(){

        OfficeDto officeDto=OfficeDto.builder()
                .id(1L)
                .build();

        EmployeeDto employeeDto=EmployeeDto.builder()
                .email("mail@email.com")
                .office(officeDto)
                .build();
        return employeeDto;
    }


    @Test
    public void findByEmail() {
        entityManager.persist(initEmployee());
//        entityManager.flush();

       Optional<Employee> employee=employeeRepository.findByEmail(initEmployee().getEmail());

        assertFalse(employee.isEmpty());
    }

    @Test
    void findAllByOfficeId() {
    }
}