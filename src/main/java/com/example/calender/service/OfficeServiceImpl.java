package com.example.calender.service;

import com.example.calender.repository.OfficeRepository;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@Transactional
public class OfficeServiceImpl implements OfficeService{

    @Autowired
    OfficeRepository officeRepository;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    MeetingRoomService meetingRoomService;
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
    public Office addNewOffice(Office office) {
            return officeRepository.save(office);
    }

    @Override
    public boolean deleteOffice(long id) throws ResourceNotFoundException{
        officeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Office","id",id));
        if(employeeService.checkEmptyOffice(id)) {
            officeRepository.deleteById(id);
            List<Long> roomsInOffice = meetingRoomService.getMeetingRoomsByOfficeId(id);
            for (Long aRoomId : roomsInOffice) meetingRoomService.deleteMeetingRoom(aRoomId);
            return true;
        }
        else return false;

    }

    @Override
    public Office updateOffice(Office office, long id) throws ResourceNotFoundException {
        Office existingOffice = officeRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Office","id",id));
        existingOffice.setLocation(office.getLocation());
        officeRepository.save(existingOffice);
        return existingOffice;
    }
}
