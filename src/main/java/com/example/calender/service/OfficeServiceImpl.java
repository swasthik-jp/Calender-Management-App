package com.example.calender.service;

import com.example.calender.repository.OfficeRepository;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService<Office>{

    @Autowired
    OfficeRepository officeRepository;
    @Override
    public List<Office> getAllBranches() {return officeRepository.findAll();}
    @Override
    public Office getOfficeById(long id) throws ResourceNotFoundException {
        Optional<Office> optionalOffice = officeRepository.findById(id);
        if(optionalOffice.isPresent())
            return optionalOffice.get();
       throw new ResourceNotFoundException("Office","id",id);
    }
    @Override
    public Office addNewOffice(Office office) throws ResourceAlreadyExistsException {
        //if(!(office.getOfficeID()!=null && officeRepository.existsById(office.getOfficeID()))){
            return officeRepository.save(office);
        //}
        //throw  new ResourceAlreadyExistsException("office","officeID",office.getOfficeID());
    }

    @Override
    public void deleteOffice(long id) throws ResourceNotFoundException{
        officeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Office","id",id));
        officeRepository.deleteById(id);
    }

    @Override
    public Office updateOffice(Office office, long id) throws ResourceNotFoundException {
        Office existingOffice = null;
        existingOffice = officeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Office","id",id));
        existingOffice.setOfficeLocation(office.getOfficeLocation());
        officeRepository.save(existingOffice);
        return existingOffice;
    }
}
