package com.example.calender.controller;

import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Office;
import com.example.calender.mapper.Mapper;
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
    Mapper<Office, OfficeDto> officeMapper;
    @Autowired
    public OfficeService<Office> officeServiceImpl;

    @GetMapping("/offices")
    List<OfficeDto> getAllBranches(){return officeServiceImpl.getAllBranches().stream()
            .map(office -> officeMapper.toDto(office))
            .collect(Collectors.toList());}

    @SneakyThrows
    @PostMapping("/office")
    ResponseEntity<OfficeDto> addNewOffice(@RequestBody OfficeDto dtoOffice){
        Office office = officeMapper.toEntity(dtoOffice);
        return new ResponseEntity<>(officeMapper.toDto(officeServiceImpl.addNewOffice(office)),HttpStatus.CREATED);
    }
    @SneakyThrows
    @GetMapping("/office/{id}")
    ResponseEntity<OfficeDto> getEmployee(@PathVariable Long id){
        return new ResponseEntity<>(officeMapper.toDto(officeServiceImpl.getOfficeById(id)), HttpStatus.OK);
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
        dtoOffice.setId(null);
        Office office = officeMapper.toEntity(dtoOffice);
        return new ResponseEntity<>(officeMapper.toDto(officeServiceImpl.updateOffice(office,id)),HttpStatus.OK);
    }

}
