package com.example.calender.service;

import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) throws ResourceAlreadyExistsException {
        try {
            return employeeRepository.save(employee);
        } catch (Exception ex) {
            throw new ResourceAlreadyExistsException("Employee", "email", employee.getEmail());
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(long id) throws ResourceNotFoundException {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    public Employee getEmployeeByEmail(String email) throws ResourceNotFoundException {
        return employeeRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));
    }

    public Employee updateEmployee(Employee employee, long id) throws ResourceNotFoundException {

        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("employee", "id", id));

        existingEmployee.setName(employee.getName());
        existingEmployee.setOffice(employee.getOffice());
        existingEmployee.setHouseAddress(employee.getHouseAddress());
        existingEmployee.setDob(employee.getDob());
        existingEmployee.setMob(employee.getMob());
        existingEmployee.setEmail(employee.getEmail());
        employeeRepository.save(existingEmployee);
        return existingEmployee;

    }

    @Override
    public void deleteEmployeeByEmail(String email) throws ResourceNotFoundException {
        Employee foundEmployee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("employee", "email", email));
        employeeRepository.deleteById(foundEmployee.getId());
    }

    public void deleteEmployeeById(long id) throws ResourceNotFoundException {
        employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("employee", "id", id));
        employeeRepository.deleteById(id);
    }


    public boolean checkEmptyOffice(Long fkOfficeId) throws ResourceNotFoundException {
        Optional<List<Employee>> officeWorkforce = employeeRepository.findAllByOfficeId(fkOfficeId);
        if (officeWorkforce.isPresent()) {
            log.debug("OfficeID: " + fkOfficeId + " has " + officeWorkforce.get().size() + " employees");
            return officeWorkforce.get().isEmpty();
        } else throw new ResourceNotFoundException("employee", "officeId", fkOfficeId);
    }


}
