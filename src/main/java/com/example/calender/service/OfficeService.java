package com.example.calender.service;
import java.util.List;

public interface OfficeService<T> {

    public List<T> getAllBranches();
    public T getOfficeById(long id);
    public T addNewOffice(T office);
    public void deleteOffice(long id);
    public T updateOffice(T office, long id);
}
