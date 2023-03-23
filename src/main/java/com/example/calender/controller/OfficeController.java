package com.example.calender.controller;

import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Office;
import com.example.calender.service.OfficeService;
import com.example.calender.service.OfficeServiceImpl;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OfficeController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public OfficeService<Office> officeServiceImpl;

    public OfficeController(OfficeServiceImpl officeServiceImpl) {
        this.officeServiceImpl = officeServiceImpl;
    }

    @GetMapping("/offices")
    List<OfficeDto> getAllBranches(){return officeServiceImpl.getAllBranches().stream()
            .map(office -> modelMapper.map(office, OfficeDto.class))
            .collect(Collectors.toList());}

    @SneakyThrows
    @PostMapping("/office")
    ResponseEntity<OfficeDto> addNewOffice(@RequestBody OfficeDto dtooffice){
        Office office = modelMapper.map(dtooffice, Office.class);
        return new ResponseEntity<>(modelMapper.map(officeServiceImpl.addNewOffice(office), OfficeDto.class),HttpStatus.CREATED);
    }
    @SneakyThrows
    @GetMapping("/office/{id}")
    ResponseEntity<OfficeDto> getEmployee(@PathVariable Long id){
        return new ResponseEntity<>(modelMapper.map(officeServiceImpl.getOfficeById(id), OfficeDto.class), HttpStatus.OK);
    }

    @SneakyThrows
    @DeleteMapping("/office/{id}")
    ResponseEntity<String> deleteOffice(@PathVariable Long  id){
        officeServiceImpl.deleteOffice(id);
        return new ResponseEntity<>("SUCCESS: Office Building Destroyed", HttpStatus.OK);
    }
    @SneakyThrows
    @PutMapping("/office/{id}")
    ResponseEntity<OfficeDto> updateEmployee(@RequestBody OfficeDto dtoOffice, @PathVariable long  id){
        Office office = modelMapper.map(dtoOffice, Office.class);
        return new ResponseEntity<>(modelMapper.map(officeServiceImpl.updateOffice(office,id), OfficeDto.class),HttpStatus.OK);
    }

}
