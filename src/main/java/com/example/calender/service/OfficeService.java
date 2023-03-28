package com.example.calender.service;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;

import java.util.List;

public interface OfficeService {

    public List<Office> getAllBranches();
    public Office getOfficeById(long id) throws ResourceNotFoundException;
    public Office addNewOffice(Office office) throws ResourceAlreadyExistsException;
    public boolean deleteOffice(long id) throws ResourceNotFoundException;
    public Office updateOffice(Office office, long id) throws ResourceNotFoundException;
}
