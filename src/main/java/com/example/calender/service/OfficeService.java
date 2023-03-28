package com.example.calender.service;

import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;

import java.util.List;

public interface OfficeService {

    List<Office> getAllBranches();

    Office getOfficeById(long id) throws ResourceNotFoundException;

    Office addNewOffice(Office office) throws ResourceAlreadyExistsException;

    boolean deleteOffice(long id) throws ResourceNotFoundException;

    Office updateOffice(Office office, long id) throws ResourceNotFoundException;
}
