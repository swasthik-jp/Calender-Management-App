package com.example.calender.service;

import com.example.calender.repository.MeetingRoomRepository;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Override
    public MeetingRoom saveMeetingRoom(MeetingRoom meetingRoom) {
        return meetingRoomRepository.save(meetingRoom);
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

    @Override
    public List<Long> getMeetingRoomsByOfficeId(long officeId) throws ResourceNotFoundException {
        Optional<List<Long>> roomsInOffice = meetingRoomRepository.findAllByOfficeId(officeId);
        if (roomsInOffice.isPresent()) {
            log.debug("office with office id " + officeId + " have " + roomsInOffice.get().size() + " meeting rooms");
            return roomsInOffice.get();
        } else throw new ResourceNotFoundException("meeting_room", "office_id", officeId);
    }
}
