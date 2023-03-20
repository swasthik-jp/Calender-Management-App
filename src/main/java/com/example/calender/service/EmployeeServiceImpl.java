package com.example.calender.service;

import com.example.calender.dao.EmployeeDao;
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
    EmployeeDao employeeDao;

    public Employee saveEmployee(Employee employee){
      if(!(employee.getId()!=null && employeeDao.existsById(employee.getId()))){
          return   employeeDao.save(employee);
      }
      throw  new ResourceAlreadyExistsException("employee","id",employee.getId());
    }

    public List<Employee> getAllEmployees(){
        return employeeDao.findAll();
    }

    public Employee getEmployeesById(long id){
        Optional<Employee> optionalEmployee= employeeDao.findById(id);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        throw new ResourceNotFoundException("employee","id",id);
    }

    public Employee getEmployeesByEmail(String email)
    {
        Optional<Employee> optionalEmployee= employeeDao.findByEmail(email);
        if(optionalEmployee.isPresent()){
            return optionalEmployee.get();
        }
        throw new ResourceNotFoundException("employee","Email",email);
    }

    public Employee updateEmployee(Employee employee, long id){

         Employee existingEmployee= employeeDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("employee","id",id));

            existingEmployee.setName(employee.getName());
            //existingEmployee.setOfficeAddress(employee.getOfficeAddress());
            existingEmployee.setOffice(employee.getOffice());
            existingEmployee.setHouseAddress(employee.getHouseAddress());
            existingEmployee.setDob(employee.getDob());
            existingEmployee.setMob(employee.getMob());
            existingEmployee.setEmail(employee.getEmail());
            employeeDao.save(existingEmployee);
            return existingEmployee;

    }

    public void deleteEmployee(long id){
        employeeDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("employee","id",id));
        employeeDao.deleteById(id);
    }


}
