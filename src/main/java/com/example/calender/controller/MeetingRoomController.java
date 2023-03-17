package com.example.calender.controller;

import com.example.calender.entity.MeetingRoom;
import com.example.calender.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetingroom")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping()
    List<MeetingRoom> getAllMeetingRooms( ){
        return meetingRoomService.getAllMeetingRooms();
    }


    @GetMapping("{id}")
    ResponseEntity<MeetingRoom> getMeetingRoom(@PathVariable Long id){
        return new ResponseEntity<>(meetingRoomService.getMeetingRoomById(id), HttpStatus.OK);
    }

    @PostMapping()
    ResponseEntity<MeetingRoom> insertMeetingRoom(@RequestBody MeetingRoom meetingRoom){
        return new ResponseEntity<>(meetingRoomService.saveMeetingRoom(meetingRoom),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteMeetingRoom(@PathVariable("id") long  id){
        meetingRoomService.deleteMeetingRoom(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }


    @PutMapping("{id}")
    ResponseEntity<MeetingRoom> updateMeetingRoom(@RequestBody MeetingRoom meetingRoom,@PathVariable("id") long  id){
        return new ResponseEntity<>(meetingRoomService.updateMeetingRoom(meetingRoom,id),HttpStatus.OK);
    }

}
