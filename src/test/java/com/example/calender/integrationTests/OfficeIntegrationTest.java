package com.example.calender.integrationTests;


import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class OfficeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfficeRepository officeRepository;

    public Office initOffice(){
        Office office=Office.builder()
                .location("banglore")
                .build();
        return office;
    }

    @Test
    public void getAllBranches_WhenReqSent_ThenOfficeParametersMatch() throws Exception {

        Office office=initOffice();
        Office savedOffice=officeRepository.save(office);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/offices")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].location", CoreMatchers.is(office.getLocation())));

    }

    @Test
    public void getOffice_WhenValidID_ThenReturnOffice()throws Exception{
        Office office=initOffice();
        Office savedOffice=officeRepository.save(office);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/office/"+office.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", CoreMatchers.is(office.getLocation())));

    }


    @Test
    public void getOffice_WhenInValidID_ThenStatusCodeMatch()throws Exception{
        Office office=initOffice();
        Office savedOffice=officeRepository.save(office);

        int invalidId=1001;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/office/"+invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void addNewOffice_WhenValidOffice_ThenSaveOffice()throws Exception{
        Office office=initOffice();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/office")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(office))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

        assertEquals(office.getLocation(),officeRepository.findAll().get(0).getLocation());
    }

    @Test
    public  void deleteOffice_WhenValidIdWithZeroEmployees_ThenDeleteOffice()throws Exception{
        Office office=initOffice();
        Office savedOffice=officeRepository.save(office);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/office/"+savedOffice.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        assertTrue(officeRepository.findById(savedOffice.getId()).isEmpty());
    }


    @Test
    public void updateOffice_WhenValidOffice_ThenUpdateOffice()throws Exception{

        Office office=initOffice();
        office.setId(2L);
        Office savedOffice=officeRepository.save(office);

        office.setLocation("Mumbai");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/office/"+savedOffice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(office))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", CoreMatchers.is(office.getLocation())));

        assertNotEquals(savedOffice.getLocation(),officeRepository.findById(savedOffice.getId()).get().getLocation());
    }

}
