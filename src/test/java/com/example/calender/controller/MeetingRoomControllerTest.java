package com.example.calender.controller;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.MeetingRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MeetingRoomController.class)
@AutoConfigureMockMvc(webClientEnabled = false,addFilters  = false)
class MeetingRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingRoomService meetingRoomService;

    @MockBean
    private Mapper<MeetingRoom, MeetingRoomDto> meetingRoomDtoMapper;

    @MockBean
    private RestTemplate restTemplate;


    public MeetingRoom initMeetingRoom(){
        Office office=Office.builder()
                .id(1L)
                .build();
        MeetingRoom meetingRoom = MeetingRoom.builder()
                .id(10L)
                .name("MeetingRoomZero")
                .capacity(10)
                .office(office)
                .build();
        return meetingRoom;
    }

    public MeetingRoomDto initMeetingRoomDto(){

        OfficeDto officeDto=OfficeDto.builder()
                .id(1L)
                .build();
        MeetingRoomDto meetingRoomDto = MeetingRoomDto.builder()
                .id(10L)
                .name("MeetingRoomZero")
                .capacity(10)
                .office(officeDto)
                .build();
        return meetingRoomDto;
    }


    @Test
    void getAllMeetingRooms_WhenReqSent_ThenMeetingRoomParametersMatch() throws Exception {

        MeetingRoom meetingRoom = initMeetingRoom();
        MeetingRoomDto meetingRoomDto = initMeetingRoomDto();

        Mockito.when(meetingRoomService.getAllMeetingRooms()).thenReturn(Arrays.asList(initMeetingRoom()));
        Mockito.when(meetingRoomDtoMapper.toDto(meetingRoom)).thenReturn(meetingRoomDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meetingrooms")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", CoreMatchers.is(meetingRoom.getName())));

    }


    @Test
    public void getMeetingRoom_WhenValidID_ThenReturnMeetingRoom()throws Exception{
        MeetingRoom meetingRoom=initMeetingRoom();
        MeetingRoomDto meetingRoomDto=initMeetingRoomDto();
        Mockito.when(meetingRoomService.getMeetingRoomById(meetingRoom.getId())).thenReturn(meetingRoom);
        Mockito.when(meetingRoomDtoMapper.toDto(meetingRoom)).thenReturn(meetingRoomDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meetingroom/"+meetingRoom.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(meetingRoom.getName())));

    }


    @Test
    public void getMeetingRoom_WhenInValidID_ThenStatusCodeMatch()throws Exception{
        MeetingRoom meetingRoom=initMeetingRoom();
        MeetingRoomDto meetingRoomDto=initMeetingRoomDto();
        Mockito.when(meetingRoomService.getMeetingRoomById(Mockito.anyLong())).thenThrow(ResourceNotFoundException.class);
        Mockito.when(meetingRoomDtoMapper.toDto(meetingRoom)).thenReturn(meetingRoomDto);

        int invalidId=1001;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meetingroom/"+invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

    }


    @Test
    public void insertMeetingRoom_WhenValidMeetingRoom_ThenSaveMeetingRoom()throws Exception{
        MeetingRoom meetingRoom=initMeetingRoom();
        MeetingRoomDto meetingRoomDto=initMeetingRoomDto();
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);
        Mockito.when(meetingRoomService.saveMeetingRoom(meetingRoom)).thenReturn(meetingRoom);
        Mockito.when(meetingRoomDtoMapper.toEntity(meetingRoomDto)).thenReturn(meetingRoom);
        Mockito.when(meetingRoomDtoMapper.toDto(meetingRoom)).thenReturn(meetingRoomDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meetingroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoom))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

    }

    @Test
    public void insertMeetingRoom_WhenInValidOfficeID_ThenThrowNotFoundStatusCode()throws Exception{
        MeetingRoom meetingRoom=initMeetingRoom();
        MeetingRoomDto meetingRoomDto=initMeetingRoomDto();
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(HttpClientErrorException.class);
        Mockito.when(meetingRoomService.saveMeetingRoom(meetingRoom)).thenReturn(meetingRoom);
        Mockito.when(meetingRoomDtoMapper.toEntity(meetingRoomDto)).thenReturn(meetingRoom);
        Mockito.when(meetingRoomDtoMapper.toDto(meetingRoom)).thenReturn(meetingRoomDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meetingroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoom))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

    }

    @Test
    public  void deleteMeetingRoom_WhenValidId_ThenDeleteMeetingRoom()throws Exception{
        MeetingRoom meetingRoom=initMeetingRoom();
        Mockito.doNothing().when(meetingRoomService).deleteMeetingRoom(meetingRoom.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/meetingroom/"+meetingRoom.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @Test
    public void updateMeetingRoom_WhenValidMeetingRoom_ThenUpdateMeetingRoom()throws Exception{

        MeetingRoom meetingRoom=initMeetingRoom();
        MeetingRoomDto meetingRoomDto=initMeetingRoomDto();
        MeetingRoom updatedMeetingRoom=initMeetingRoom();
        updatedMeetingRoom.setName("meetingRoomZero");
        MeetingRoomDto updatedMeetingRoomDto=initMeetingRoomDto();
        updatedMeetingRoomDto.setName("meetingRoomZero");

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);
        Mockito.when(meetingRoomService.updateMeetingRoom(meetingRoom,meetingRoom.getId())).thenReturn(updatedMeetingRoom);
        Mockito.when(meetingRoomDtoMapper.toEntity(meetingRoomDto)).thenReturn(meetingRoom);
        Mockito.when(meetingRoomDtoMapper.toDto(updatedMeetingRoom)).thenReturn(updatedMeetingRoomDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/meetingroom/"+meetingRoom.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoomDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(updatedMeetingRoom.getName())));

    }


    @Test
    public void updateMeetingRoom_WhenInValidOfficeID_ThenThrowNotFoundStatusCode()throws Exception{

        MeetingRoom meetingRoom=initMeetingRoom();
        MeetingRoomDto meetingRoomDto=initMeetingRoomDto();
        MeetingRoom updatedMeetingRoom=initMeetingRoom();
        updatedMeetingRoom.setName("meetingRoomZero");
        MeetingRoomDto updatedMeetingRoomDto=initMeetingRoomDto();
        updatedMeetingRoomDto.setName("meetingRoomZero");

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(HttpClientErrorException.class);
        Mockito.when(meetingRoomService.updateMeetingRoom(meetingRoom,meetingRoom.getId())).thenReturn(updatedMeetingRoom);
        Mockito.when(meetingRoomDtoMapper.toEntity(meetingRoomDto)).thenReturn(meetingRoom);
        Mockito.when(meetingRoomDtoMapper.toDto(updatedMeetingRoom)).thenReturn(updatedMeetingRoomDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/meetingroom/"+meetingRoom.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoomDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }
}