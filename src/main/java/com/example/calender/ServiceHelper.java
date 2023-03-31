package com.example.calender;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.entity.Attendee;
import com.example.calender.entity.Employee;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.AttendeeRepository;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ServiceHelper {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    MeetingRoomService meetingRoomService;


    @Autowired
    AttendeeRepository attendeeRepository;





     public Set<String> employeeToJustEmails(Set<Attendee> attendees)
     {
         Set<String> emailsOfEmployees = new HashSet<>();
         for(Attendee attendee:attendees)
         {
             emailsOfEmployees.add(attendee.getEmployee().getEmail());
         }
         return emailsOfEmployees;
     }

     public Employee getEmployeeById(Long id) throws ResourceNotFoundException {
         return employeeService.getEmployeeById(id);
     }

     public MeetingRoom getMeetingRoomById(Long id) throws ResourceNotFoundException {
         return  meetingRoomService.getMeetingRoomById(id);
     }


     public Set<Attendee> getAllAttendees(Set<String> emails) throws ResourceNotFoundException {
         Set<Attendee> attendeeSet = new HashSet<>();
         Attendee a = new Attendee(12L,employeeService.getEmployeeById(1), AttendingStatus.NO);
         a = attendeeRepository.save(a);
         attendeeSet.add(a);
         return attendeeSet;

     }
}

