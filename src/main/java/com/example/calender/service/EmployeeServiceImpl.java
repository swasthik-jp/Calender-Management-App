package com.example.calender.service;

import com.example.calender.repository.EmployeeRepository;
import com.example.calender.entity.Employee;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) throws ResourceAlreadyExistsException {
      if(!(employee.getId()!=null && employeeRepository.existsById(employee.getId()))){
          return   employeeRepository.save(employee);
      }
      throw  new ResourceAlreadyExistsException("employee","id",employee.getId());
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(long id) throws ResourceNotFoundException {
        Optional<Employee> optionalEmployee= employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        throw new ResourceNotFoundException("employee","id",id);
    }

    public Employee getEmployeeByEmail(String email) throws ResourceNotFoundException
    {
        Optional<Employee> optionalEmployee= employeeRepository.findByEmail(email);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        throw new ResourceNotFoundException("employee","Email",email);
    }

    public Employee updateEmployee(Employee employee, long id) throws  ResourceNotFoundException{

         Employee existingEmployee= employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("employee","id",id));

            existingEmployee.setName(employee.getName());
            //existingEmployee.setOfficeAddress(employee.getOfficeAddress());
            existingEmployee.setOffice(employee.getOffice());
            existingEmployee.setHouseAddress(employee.getHouseAddress());
            existingEmployee.setDob(employee.getDob());
            existingEmployee.setMob(employee.getMob());
            existingEmployee.setEmail(employee.getEmail());
            employeeRepository.save(existingEmployee);
            return existingEmployee;

    }

    public void deleteEmployee(long id) throws ResourceNotFoundException{
        employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("employee","id",id));
        employeeRepository.deleteById(id);
    }


}
