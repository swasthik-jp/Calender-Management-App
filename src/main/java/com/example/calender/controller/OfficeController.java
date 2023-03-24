package com.example.calender.controller;

import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Office;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.OfficeService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OfficeController {
    @Autowired
    private Mapper<Office, OfficeDto> officeMapper;
    @Autowired
    private OfficeService<Office> officeService;

    @GetMapping("/offices")
    List<OfficeDto> getAllBranches(){
        return officeService.getAllBranches().stream()
            .map(office -> officeMapper.toDto(office))
            .collect(Collectors.toList());
    }

    @SneakyThrows
    @PostMapping("/office")
    ResponseEntity<OfficeDto> addNewOffice(@RequestBody OfficeDto dtoOffice){
        dtoOffice.setId(null);
        Office office = officeMapper.toEntity(dtoOffice);
        return new ResponseEntity<>(officeMapper.toDto(officeService.addNewOffice(office)),HttpStatus.CREATED);
    }
    @SneakyThrows
    @GetMapping("/office/{id}")
    ResponseEntity<OfficeDto> getOffice(@PathVariable Long id){
        return new ResponseEntity<>(officeMapper.toDto(officeService.getOfficeById(id)), HttpStatus.OK);
    }

    @SneakyThrows
    @DeleteMapping("/office/{id}")
    ResponseEntity<String> deleteOffice(@PathVariable Long  id){
        if(officeService.deleteOffice(id))
            return new ResponseEntity<>("SUCCESS: Office Building Destroyed", HttpStatus.OK);
        else
            return new ResponseEntity<>("FAILURE: Employee are still present", HttpStatus.BAD_REQUEST);
    }
    @SneakyThrows
    @PutMapping("/office/{id}")
    ResponseEntity<OfficeDto> updateEmployee(@RequestBody OfficeDto dtoOffice, @PathVariable long  id){
        dtoOffice.setId(null);
        Office office = officeMapper.toEntity(dtoOffice);
        return new ResponseEntity<>(officeMapper.toDto(officeService.updateOffice(office,id)),HttpStatus.OK);
    }

}
