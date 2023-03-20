package com.example.calender.service;

import com.example.calender.dao.MeetingRoomDao;
import com.example.calender.entity.Employee;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.ResourceAlreadyExistsException;
import com.example.calender.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService{

    @Autowired
    private MeetingRoomDao meetingRoomDao;

    @Override
    public MeetingRoom saveMeetingRoom(MeetingRoom meetingRoom) {
        if(!(meetingRoom.getId()!=null && meetingRoomDao.existsById(meetingRoom.getId()))){
            return   meetingRoomDao.save(meetingRoom);
        }
        throw  new ResourceAlreadyExistsException("meetingRoom","id",meetingRoom.getId());
    }

    @Override
    public List<MeetingRoom> getAllMeetingRooms() {
        return meetingRoomDao.findAll();
    }

    @Override
    public MeetingRoom getMeetingRoomById(long id) {
        Optional<MeetingRoom> optionalMeetingRoom= meetingRoomDao.findById(id);
        if(optionalMeetingRoom.isPresent()){
            return optionalMeetingRoom.get();
        }
        throw new ResourceNotFoundException("meetingRoom","id",id);
    }

    @Override
    public MeetingRoom updateMeetingRoom(MeetingRoom meetingRoom, long id) {
        MeetingRoom existingMeetingRoom= meetingRoomDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("MeetingRoom","id",id));

        existingMeetingRoom.setName(meetingRoom.getName());
        existingMeetingRoom.setOffice(meetingRoom.getOffice());
        existingMeetingRoom.setCapacity(meetingRoom.getCapacity());
        existingMeetingRoom.setOperational(meetingRoom.isOperational());

         meetingRoomDao.save(existingMeetingRoom);
        return existingMeetingRoom;
    }

    @Override
    public void deleteMeetingRoom(long id) {
        meetingRoomDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("MeetingRoom","id",id));
        meetingRoomDao.deleteById(id);
    }
}
