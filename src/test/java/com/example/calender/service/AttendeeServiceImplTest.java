package com.example.calender.service;

import com.example.calender.entity.Attendee;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import com.example.calender.repository.AttendeeRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class AttendeeServiceImplTest {

    @Mock
    AttendeeRepository attendeeRepository;

    @InjectMocks
    AttendeeServiceImpl attendeeService;

    public Office initOffice(){
        Office office=Office.builder()
                .id(1L)
                .build();
        return  office;
    }

    public Employee initEmployee(Office office){

        Employee testEmp = Employee.builder()
                .id(10L)
                .name("employee1")
                .email("mail@email.com")
                .office(office)
                .build();
        return testEmp;
    }

    Attendee initAttendee(Employee employee){
        return Attendee.builder().employee(employee).id(1L).build();
    }

   @Test
   public void getAllAttendees_WhenRepositoryReturnsAttendees_ThenReturnAttendee() {

        Office office=initOffice();
        Employee employee=initEmployee(office);
        Attendee attendee=initAttendee(employee);

        Mockito.when(attendeeRepository.findAll()).thenReturn(Arrays.asList(attendee));

       assertEquals(Arrays.asList(attendee), attendeeService.getAllAttendees());

    }

   @Test
   public void save_WhenAttendeeSaved_ThenReturnAttendee() {
        Office office=initOffice();
        Employee employee=initEmployee(office);
        Attendee attendee=initAttendee(employee);

        Mockito.when(attendeeRepository.save(attendee)).thenReturn(attendee);

        assertEquals(attendee, attendeeService.save(attendee));

    }
}