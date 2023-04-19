package com.example.calender.integrationTests;

import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.EmployeeRepository;
import com.example.calender.repository.MeetingRoomRepository;
import com.example.calender.repository.OfficeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MeetingRoomIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private OfficeRepository officeRepository;

    public Office initOffice(){
        Office office=Office.builder()
                .location("banglore")
                .build();
        return office;
    }

    public MeetingRoom initMeetingRoom(Office office){

        MeetingRoom meetingRoom = MeetingRoom.builder()
                .id(10L)
                .name("MeetingRoomZero")
                .capacity(10)
                .office(office)
                .build();
        return meetingRoom;
    }


    @Test
    public void getAllMeetingRooms_WhenReqSent_ThenMeetingRoomParametersMatch() throws Exception {

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom= meetingRoomRepository.save(meetingRoom);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meetingrooms")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", CoreMatchers.is(savedMeetingRoom.getName())));

    }

    @Test
    public void getMeetingRoom_WhenValidID_ThenReturnMeetingRoom()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom= meetingRoomRepository.save(meetingRoom);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meetingroom/"+savedMeetingRoom.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(savedMeetingRoom.getName())));

    }


    @Test
    public void getMeetingRoom_WhenInValidID_ThenStatusCodeMatch()throws Exception{

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom= meetingRoomRepository.save(meetingRoom);

        int invalidId=1001;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/meetingroom/"+invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

    }


    @Test
    public void insertMeetingRoom_WhenValidMeetingRoom_ThenSaveMeetingRoom()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meetingroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoom))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

        assertEquals(meetingRoom.getName(),meetingRoomRepository.findAll().get(0).getName());

    }


    @Test
    public void insertMeetingRoom_WhenInValidOfficeID_ThenThrowNotFoundStatusCode()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(HttpClientErrorException.class);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/meetingroom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoom))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

        assertTrue(meetingRoomRepository.findAll().isEmpty());

    }


    @Test
    public  void deleteMeetingRoom_WhenValidId_ThenDeleteMeetingRoom()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);
        MeetingRoom savedMeetingRoom= meetingRoomRepository.save(meetingRoom);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/meetingroom/"+savedMeetingRoom.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        assertTrue(meetingRoomRepository.findById(savedMeetingRoom.getId()).isEmpty());

    }


    @Test
    public void updateMeetingRoom_WhenValidMeetingRoom_ThenUpdateMeetingRoom()throws Exception{

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);
        meetingRoom.setId(100L);
        MeetingRoom savedMeetingRoom= meetingRoomRepository.save(meetingRoom);

        meetingRoom.setName("RoomOne");

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/meetingroom/"+savedMeetingRoom.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoom))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(meetingRoom.getName())));

        assertNotEquals(meetingRoom.getName(),savedMeetingRoom.getName());

    }

    @Test
    public void updateMeetingRoom_WhenInValidOfficeID_ThenThrowNotFoundStatusCode()throws Exception{

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        MeetingRoom meetingRoom = initMeetingRoom(savedOffice);
        meetingRoom.setId(100L);
        MeetingRoom savedMeetingRoom= meetingRoomRepository.save(meetingRoom);

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(HttpClientErrorException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/meetingroom/"+meetingRoom.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(meetingRoom))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }


}
