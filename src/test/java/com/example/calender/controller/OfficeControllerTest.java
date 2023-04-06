package com.example.calender.controller;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.OfficeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
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
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OfficeController.class)
@AutoConfigureMockMvc(webClientEnabled = false,addFilters  = false)
public class OfficeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfficeService officeService;

    @MockBean
    private Mapper<Office, OfficeDto> officeDtoMapper;

    public Office initOffice(){
        Office office=Office.builder()
                .id(1L)
                .location("banglore")
                .build();
        return office;
    }

    public OfficeDto initOfficeDto(){

        OfficeDto officeDto=OfficeDto.builder()
                .id(1L)
                .location("banglore")
                .build();

        return officeDto;
    }


    @Test
    public void getAllBranches_WhenReqSent_ThenOfficeParametersMatch() throws Exception {

        Office office=initOffice();
        OfficeDto officeDto=initOfficeDto();

        Mockito.when(officeService.getAllBranches()).thenReturn(Arrays.asList(office));
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

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
        OfficeDto officeDto=initOfficeDto();
        Mockito.when(officeService.getOfficeById(Mockito.anyLong())).thenReturn(office);
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

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
        OfficeDto officeDto=initOfficeDto();
        Mockito.when(officeService.getOfficeById(Mockito.anyLong())).thenThrow(ResourceNotFoundException.class);
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

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
        OfficeDto officeDto=initOfficeDto();

        Mockito.when(officeService.addNewOffice(office)).thenReturn(office);
        Mockito.when(officeDtoMapper.toEntity(officeDto)).thenReturn(office);
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/office")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(office))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

    }

    @Test
    public  void deleteOffice_WhenValidIdWithZeroEmployees_ThenDeleteOffice()throws Exception{
        Office office=initOffice();
        OfficeDto officeDto=initOfficeDto();
        Mockito.when(officeService.deleteOffice(office.getId())).thenReturn(true);
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/office/"+office.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @Test
    public  void deleteOffice_WhenValidIdWithEmployees_ThrowBadRequestStatusCode()throws Exception{
        Office office=initOffice();
        OfficeDto officeDto=initOfficeDto();
        Mockito.when(officeService.deleteOffice(office.getId())).thenReturn(false);
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/office/"+office.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateOffice_WhenValidOffice_ThenUpdateOffice()throws Exception{

        Office office=initOffice();
        OfficeDto officeDto=initOfficeDto();
        officeDto.setId(null);
        Mockito.when(officeService.updateOffice(office,office.getId())).thenReturn(office);
        Mockito.when(officeDtoMapper.toEntity(officeDto)).thenReturn(office);
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/office/"+office.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(officeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.location", CoreMatchers.is(office.getLocation())));

    }


    @Test
    public void updateOffice_WhenInValidOffice_ThenUpdateOffice()throws Exception{

        Office office=initOffice();
        OfficeDto officeDto=initOfficeDto();
        officeDto.setId(null);
        Mockito.when(officeService.updateOffice(office,office.getId())).thenThrow(ResourceNotFoundException.class);
        Mockito.when(officeDtoMapper.toEntity(officeDto)).thenReturn(office);
        Mockito.when(officeDtoMapper.toDto(office)).thenReturn(officeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/office/"+office.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(officeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

    }



}