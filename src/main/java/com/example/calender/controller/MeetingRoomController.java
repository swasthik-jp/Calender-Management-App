package com.example.calender.controller;

import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.MeetingRoomService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
public class MeetingRoomController {

    @Autowired
    private Mapper<MeetingRoom, MeetingRoomDto> meetingRoomDtoMapper;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private RestTemplate restTemplate;

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
        try { //check if the office exists or soft deleted
            restTemplate.getForObject(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/office/" + dtoMeetingRoom.getOffice().getId(), OfficeDto.class);
        } catch (HttpClientErrorException e) { // return BAD REQUEST if office is soft deleted
            throw new ResourceNotFoundException("Office", "id", dtoMeetingRoom.getOffice().getId());
        }
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
        try { //check if the office exists or soft deleted
            restTemplate.getForObject(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/office/" + dtoMeetingRoom.getOffice().getId(), OfficeDto.class);
        } catch (HttpClientErrorException e) { // return BAD REQUEST if office is soft deleted
            throw new ResourceNotFoundException("Office", "id", dtoMeetingRoom.getOffice().getId());
        }
        MeetingRoom meetingRoom = meetingRoomDtoMapper.toEntity(dtoMeetingRoom);
        return new ResponseEntity<>(meetingRoomDtoMapper.toDto(meetingRoomService.updateMeetingRoom(meetingRoom, id)), HttpStatus.OK);
    }

}
