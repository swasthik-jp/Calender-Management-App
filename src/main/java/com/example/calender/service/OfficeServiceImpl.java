package com.example.calender.service;

import com.example.calender.dao.OfficeDao;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService<Office>{

    @Autowired
    OfficeDao officeDao;
    @Override
    public List<Office> getAllBranches() {return officeDao.findAll();}
    @Override
    public Office getOfficeById(long id) {
        Optional<Office> optionalOffice = officeDao.findById(id);
        if(optionalOffice.isPresent())
            return optionalOffice.get();
        else throw new ResourceNotFoundException("Office","id",id);
    }
    @Override
    public Office addNewOffice(Office office) { return officeDao.save(office);}

    @Override
    public void deleteOffice(long id) {
        officeDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("Office","id",id));
        officeDao.deleteById(id);
    }

    @Override
    public Office updateOffice(Office office, long id) {
        Office existingOffice = officeDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("Office","id",id));
        existingOffice.setNoOfEmployees(office.getNoOfEmployees());
        existingOffice.setOfficeLocation(office.getOfficeLocation());
        officeDao.save(existingOffice);
        return existingOffice;
    }
}
