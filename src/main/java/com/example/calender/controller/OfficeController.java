package com.example.calender.controller;

import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import com.example.calender.service.OfficeService;
import com.example.calender.service.OfficeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/office")
public class OfficeController {

    @Autowired
    public OfficeService<Office> officeServiceImpl;

    public OfficeController(OfficeServiceImpl officeServiceImpl) {
        this.officeServiceImpl = officeServiceImpl;
    }

    @GetMapping()
    List<Office> getAllBranches(){return officeServiceImpl.getAllBranches();}

    @PostMapping()
    ResponseEntity<Office> addNewOffice(@RequestBody Office office){
        return new ResponseEntity<>(officeServiceImpl.addNewOffice(office),HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    ResponseEntity<Office> getEmployee(@PathVariable Long id){
        return new ResponseEntity<>(officeServiceImpl.getOfficeById(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteOffice(@PathVariable("id") long  id){
        officeServiceImpl.deleteOffice(id);
        return new ResponseEntity<>("SUCCESS: Office Building Destroyed", HttpStatus.OK);
    }
    @PutMapping("{id}")
    ResponseEntity<Office> updateEmployee(@RequestBody Office office,@PathVariable("id") long  id){
        return new ResponseEntity<>(officeServiceImpl.updateOffice(office,id),HttpStatus.OK);
    }


}
