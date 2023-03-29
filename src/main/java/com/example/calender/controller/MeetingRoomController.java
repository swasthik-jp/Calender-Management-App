package com.example.calender.controller;

import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.MeetingRoomService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MeetingRoomController {

    @Autowired
    private Mapper<MeetingRoom, MeetingRoomDto> meetingRoomDtoMapper;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping("/meetingrooms")
    List<MeetingRoomDto> getAllMeetingRooms() {
        return meetingRoomService.getAllMeetingRooms().stream().map(meetingRoom -> meetingRoomDtoMapper.toDto(meetingRoom)).toList();
    }


    @SneakyThrows
    @GetMapping("/meetingroom/{id}")
    ResponseEntity<MeetingRoomDto> getMeetingRoom(@PathVariable Long id) {
        return new ResponseEntity<>(meetingRoomDtoMapper.toDto(meetingRoomService.getMeetingRoomById(id)), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/meetingroom")
    ResponseEntity<MeetingRoomDto> insertMeetingRoom(@Valid @RequestBody MeetingRoomDto dtoMeetingRoom) {
        MeetingRoom meetingRoom = meetingRoomDtoMapper.toEntity(dtoMeetingRoom);
        return new ResponseEntity<>(meetingRoomDtoMapper.toDto(meetingRoomService.saveMeetingRoom(meetingRoom)), HttpStatus.CREATED);
    }

    @SneakyThrows
    @DeleteMapping("/meetingroom/{id}")
    ResponseEntity<String> deleteMeetingRoom(@PathVariable("id") long id) {
        meetingRoomService.deleteMeetingRoom(id);
        return new ResponseEntity<>("SUCCESS: Employee deleted successfully", HttpStatus.OK);
    }


    @SneakyThrows
    @PutMapping("/meetingroom/{id}")
    ResponseEntity<MeetingRoomDto> updateMeetingRoom(@Valid @RequestBody MeetingRoomDto dtoMeetingRoom, @PathVariable("id") long id) {
        MeetingRoom meetingRoom = meetingRoomDtoMapper.toEntity(dtoMeetingRoom);
        return new ResponseEntity<>(meetingRoomDtoMapper.toDto(meetingRoomService.updateMeetingRoom(meetingRoom, id)), HttpStatus.OK);
    }

}
