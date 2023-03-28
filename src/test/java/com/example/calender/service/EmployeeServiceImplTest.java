package com.example.calender.service;

import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService = new EmployeeServiceImpl();


    @Test
    public void when_saveEmployeeIsCalled_thenExpectSavedEmployeeObject() throws ResourceAlreadyExistsException {
        Employee testEmp = Employee.builder()
                .id(10L)
                .email("email@email.com")
                .build();
        when(employeeRepository.save(testEmp)).thenReturn(testEmp);
        Assert.assertEquals(testEmp.getEmail(), employeeService.saveEmployee(testEmp).getEmail());
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void when_saveEmployeeIsCalled_thenExpectResourceAlreadyExistsException() throws ResourceAlreadyExistsException {
        Employee testEmp = Employee.builder()
                .id(10L)
                .email("email@email.com")
                .build();
        when(employeeRepository.save(testEmp)).thenThrow(new RuntimeException());
        employeeService.saveEmployee(testEmp).getEmail();
    }

    @Test
    public void when_getEmployeeByIdIsCalled_thenExpectEmployeeObject() throws ResourceNotFoundException {
        Employee testEmp = Employee.builder()
                .id(10L)
                .email("email@email.com")
                .build();
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(testEmp));
        Assert.assertEquals(testEmp.getEmail(), employeeService.getEmployeeById(10L).getEmail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void when_getEmployeeByIdIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        employeeService.getEmployeeById(10L);
    }

    @Test
    public void when_getEmployeeByEmailIsCalled_thenExpectEmployeeObject() throws ResourceNotFoundException {
        Employee testEmp = Employee.builder()
                .id(10L)
                .email("email@email.com")
                .build();
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.of(testEmp));
        Assert.assertEquals(testEmp.getId(), employeeService.getEmployeeByEmail("email@email.com").getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void when_getEmployeeByEmailIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {

        when(employeeRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());
        employeeService.getEmployeeByEmail("email@email.com");
    }

    @Test
    public void when_updateEmployeeIsCalled_thenExpectUpdatedEmployeeObject() throws ResourceNotFoundException {
        Employee testEmp = Employee.builder()
                .id(10L)
                .email("email@email.com")
                .build();
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(new Employee()));
        when(employeeRepository.save(any())).thenReturn(testEmp);

        Assert.assertEquals("email@email.com", employeeService.updateEmployee(testEmp, 10L).getEmail());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void when_updateEmployeeIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {
        Employee testEmp = Employee.builder()
                .id(10L)
                .email("email@email.com")
                .build();
        when(employeeRepository.findById(10L)).thenReturn(Optional.empty());
        employeeService.updateEmployee(testEmp, 10L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void when_deleteEmployeeByIdIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        employeeService.deleteEmployeeById(10L);
    }
    @Test(expected = ResourceNotFoundException.class)
    public void when_deleteEmployeeByEmailIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {
        when(employeeRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        employeeService.deleteEmployeeByEmail("email@email.com");
    }

    @Test
    public void when_checkEmptyOfficeIsCalled_thenExpectTrueIfEmpty() throws ResourceNotFoundException {
        when(employeeRepository.findAllByOfficeId(anyLong())).thenReturn(Optional.of(new ArrayList<>()));
        Assert.assertTrue(employeeService.checkEmptyOffice(10L));
    }
    @Test
    public void when_checkEmptyOfficeIsCalled_thenExpectFalseIfNotEmpty() throws ResourceNotFoundException {
        when(employeeRepository.findAllByOfficeId(anyLong())).thenReturn(Optional.of(List.of(new Employee())));
        Assert.assertFalse(employeeService.checkEmptyOffice(10L));
    }
    @Test(expected = ResourceNotFoundException.class)
    public void when_checkEmptyOfficeIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {
        when(employeeRepository.findAllByOfficeId(anyLong())).thenReturn(Optional.empty());
        employeeService.checkEmptyOffice(10L);
    }
}