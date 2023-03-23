package com.example.calender.service;

import com.example.calender.repository.MeetingRoomRepository;
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
    private MeetingRoomRepository meetingRoomRepository;

    @Override
    public MeetingRoom saveMeetingRoom(MeetingRoom meetingRoom) throws ResourceAlreadyExistsException {
        //if(!(meetingRoom.getId()!=null && meetingRoomRepository.existsById(meetingRoom.getId()))){
            return   meetingRoomRepository.save(meetingRoom);
        //}
        //throw  new ResourceAlreadyExistsException("meetingRoom","id",meetingRoom.getId());
    }

    @Override
    public List<MeetingRoom> getAllMeetingRooms() {
        return meetingRoomRepository.findAll();
    }

    @Override
    public MeetingRoom getMeetingRoomById(long id) throws ResourceNotFoundException {
        Optional<MeetingRoom> optionalMeetingRoom= meetingRoomRepository.findById(id);
        if(optionalMeetingRoom.isPresent()){
            return optionalMeetingRoom.get();
        }
        throw new ResourceNotFoundException("meetingRoom","id",id);
    }

    @Override
    public MeetingRoom updateMeetingRoom(MeetingRoom meetingRoom, long id) throws ResourceNotFoundException{
        MeetingRoom existingMeetingRoom= null;
        existingMeetingRoom = meetingRoomRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("MeetingRoom","id",id));


        existingMeetingRoom.setName(meetingRoom.getName());
        existingMeetingRoom.setOffice(meetingRoom.getOffice());
        existingMeetingRoom.setCapacity(meetingRoom.getCapacity());
        existingMeetingRoom.setOperational(meetingRoom.isOperational());

         meetingRoomRepository.save(existingMeetingRoom);
        return existingMeetingRoom;
    }

    @Override
    public void deleteMeetingRoom(long id) throws ResourceNotFoundException {
        meetingRoomRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("MeetingRoom","id",id));
        meetingRoomRepository.deleteById(id);
    }
}
