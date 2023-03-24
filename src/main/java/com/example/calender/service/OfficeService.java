package com.example.calender.service;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;

import java.util.List;

public interface OfficeService<T> {

    public List<T> getAllBranches();
    public T getOfficeById(long id) throws ResourceNotFoundException;
    public T addNewOffice(T office) throws ResourceAlreadyExistsException;
    public boolean deleteOffice(long id) throws ResourceNotFoundException;
    public T updateOffice(T office, long id) throws ResourceNotFoundException;
}
