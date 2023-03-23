package com.example.calender.controller;

import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.service.MeetingRoomService;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class MeetingRoomController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping("/meetingrooms")
    List<MeetingRoomDto> getAllMeetingRooms( ){
        return meetingRoomService.getAllMeetingRooms().stream()
                .map(meetingRoom -> modelMapper.map(meetingRoom, MeetingRoomDto.class))
                .toList();
    }


    @SneakyThrows
    @GetMapping("/meetingroom/{id}")
    ResponseEntity<MeetingRoomDto> getMeetingRoom(@PathVariable Long id){
        return new ResponseEntity<>(modelMapper.map(meetingRoomService.getMeetingRoomById(id), MeetingRoomDto.class), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/meetingroom")
    ResponseEntity<MeetingRoomDto> insertMeetingRoom(@RequestBody MeetingRoomDto dtoMeetingRoom){
        dtoMeetingRoom.setId(null);
        MeetingRoom meetingRoom = modelMapper.map(dtoMeetingRoom, MeetingRoom.class);
        return new ResponseEntity<>(modelMapper.map(meetingRoomService.saveMeetingRoom(meetingRoom), MeetingRoomDto.class),HttpStatus.CREATED);
    }

    @SneakyThrows
    @DeleteMapping("/meetingroom/{id}")
    ResponseEntity<String> deleteMeetingRoom(@PathVariable("id") long  id){
        meetingRoomService.deleteMeetingRoom(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }


    @SneakyThrows
    @PutMapping("/meetingroom/{id}")
    ResponseEntity<MeetingRoomDto> updateMeetingRoom(@RequestBody MeetingRoomDto dtoMeetingRoom, @PathVariable("id") long  id){
        dtoMeetingRoom.setId(null);
        MeetingRoom meetingRoom = modelMapper.map(dtoMeetingRoom, MeetingRoom.class);
        return new ResponseEntity<>(modelMapper.map(meetingRoomService.updateMeetingRoom(meetingRoom,id), MeetingRoomDto.class),HttpStatus.OK);
    }

}
