package com.example.calender.service;

import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    public static final String EMP = "Employee";
    public static final String EMAIL = "email";


    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) throws ResourceAlreadyExistsException {
        try {
            Employee newEmployee = employeeRepository.save(employee);
            log.debug(employee.getName() + " is a new employee with id " + newEmployee.getId());
            return newEmployee;
        } catch (Exception ex) {
            throw new ResourceAlreadyExistsException(EMP, EMAIL, employee.getEmail());
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(long id) throws ResourceNotFoundException {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(EMP, "id", id));
    }

    @Override
    public Employee getEmployeeByEmail(String email) throws ResourceNotFoundException {
        return employeeRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(EMP, EMAIL, email));
    }

    @Override
    public Employee updateEmployee(Employee employee, long id) throws ResourceNotFoundException {

        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(EMP, "id", id));

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
                .orElseThrow(() -> new ResourceNotFoundException(EMP, EMAIL, email));
        employeeRepository.deleteById(foundEmployee.getId());
    }

    @Override
    public void deleteEmployeeById(long id) throws ResourceNotFoundException {
        employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EMP, "id", id));
        employeeRepository.deleteById(id);
    }


    @Override
    public boolean checkEmptyOffice(Long fkOfficeId) throws ResourceNotFoundException {
        Optional<List<Employee>> officeWorkforce = employeeRepository.findAllByOfficeId(fkOfficeId);
        if (officeWorkforce.isPresent()) {
            log.debug("OfficeID " + fkOfficeId + " has " + officeWorkforce.get().size() + " employees");
            return officeWorkforce.get().isEmpty();
        } else throw new ResourceNotFoundException(EMP, "officeId", fkOfficeId);
    }

}
