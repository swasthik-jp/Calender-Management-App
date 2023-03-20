package com.example.calender.controller;

import com.example.calender.dto.dtoMeetingRoom;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.service.MeetingRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meetingroom")
public class MeetingRoomController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping()
    List<dtoMeetingRoom> getAllMeetingRooms( ){
        return meetingRoomService.getAllMeetingRooms().stream()
                .map(meetingRoom -> modelMapper.map(meetingRoom,dtoMeetingRoom.class))
                .collect(Collectors.toList());
    }


    @GetMapping("{id}")
    ResponseEntity<dtoMeetingRoom> getMeetingRoom(@PathVariable Long id){
        return new ResponseEntity<>(modelMapper.map(meetingRoomService.getMeetingRoomById(id),dtoMeetingRoom.class), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<dtoMeetingRoom> insertMeetingRoom(@RequestBody dtoMeetingRoom dtoMeetingRoom){
        MeetingRoom meetingRoom = modelMapper.map(dtoMeetingRoom, MeetingRoom.class);
        return new ResponseEntity<>(modelMapper.map(meetingRoomService.saveMeetingRoom(meetingRoom), dtoMeetingRoom.class),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteMeetingRoom(@PathVariable("id") long  id){
        meetingRoomService.deleteMeetingRoom(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }


    @PutMapping("{id}")
    ResponseEntity<dtoMeetingRoom> updateMeetingRoom(@RequestBody dtoMeetingRoom dtoMeetingRoom,@PathVariable("id") long  id){
        MeetingRoom meetingRoom = modelMapper.map(dtoMeetingRoom, MeetingRoom.class);
        return new ResponseEntity<>(modelMapper.map(meetingRoomService.updateMeetingRoom(meetingRoom,id), dtoMeetingRoom.class),HttpStatus.OK);
    }

}
