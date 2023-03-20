package com.example.calender.controller;

import com.example.calender.dto.dtoOffice;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import com.example.calender.service.OfficeService;
import com.example.calender.service.OfficeServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/office")
public class OfficeController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public OfficeService<Office> officeServiceImpl;

    public OfficeController(OfficeServiceImpl officeServiceImpl) {
        this.officeServiceImpl = officeServiceImpl;
    }

    @GetMapping()
    List<dtoOffice> getAllBranches(){return officeServiceImpl.getAllBranches().stream()
            .map(office -> modelMapper.map(office, dtoOffice.class))
            .collect(Collectors.toList());}

    @PostMapping()
    ResponseEntity<dtoOffice> addNewOffice(@RequestBody dtoOffice dtooffice){
        Office office = modelMapper.map(dtooffice, Office.class);
        return new ResponseEntity<>(modelMapper.map(officeServiceImpl.addNewOffice(office), dtoOffice.class),HttpStatus.CREATED);
    }
    @GetMapping("{id}")
    ResponseEntity<dtoOffice> getEmployee(@PathVariable Long id){
        return new ResponseEntity<>(modelMapper.map(officeServiceImpl.getOfficeById(id), dtoOffice.class), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteOffice(@PathVariable("id") long  id){
        officeServiceImpl.deleteOffice(id);
        return new ResponseEntity<>("SUCCESS: Office Building Destroyed", HttpStatus.OK);
    }
    @PutMapping("{id}")
    ResponseEntity<dtoOffice> updateEmployee(@RequestBody dtoOffice dtoOffice,@PathVariable("id") long  id){
        Office office = modelMapper.map(dtoOffice, Office.class);
        return new ResponseEntity<>(modelMapper.map(officeServiceImpl.updateOffice(office,id),com.example.calender.dto.dtoOffice.class),HttpStatus.OK);
    }

}
