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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;


    public Employee initEmployee(Office office){

        Employee testEmp = Employee.builder()
                .id(1L)
                .email("mail@email.com")
                .office(office)
                .build();
        return testEmp;
    }

    public Office initOffice(){
        Office office=Office.builder()
                .location("banglore")
                .build();
        return office;
    }




    @Test
    public void findByEmail_WhenValidEmail_ThenReturnEmployee() {

        Office office=initOffice();

       Office  savedOffice= entityManager.merge(office);
        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=entityManager.merge(employee);

       Optional<Employee> result=employeeRepository.findByEmail(savedEmployee.getEmail());

        assertFalse(result.isEmpty());
        assertEquals(savedEmployee.getEmail(),result.get().getEmail());
    }


    @Test
    public void findByEmail_WhenInValidEmail_ThenReturnNull() {

        Office office=initOffice();

        Office  savedOffice= entityManager.merge(office);

        Employee employee=initEmployee(savedOffice);
        entityManager.merge(employee);

        String email="nonpresent@email.com";

        Optional<Employee> result=employeeRepository.findByEmail(email);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByOfficeId_WhenValidOfficeId_ThenReturnListOfEmployees() {

        Office office=initOffice();

        Office  savedOffice= entityManager.merge(office);
        Employee employee=initEmployee(savedOffice);
      Employee savedEmployee=entityManager.merge(employee);

        Optional<List<Employee>> result=employeeRepository.findAllByOfficeId(savedOffice.getId());

        assertFalse(result.isEmpty());
        assertEquals(Arrays.asList(savedEmployee),result.get());

    }
}