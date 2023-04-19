package com.example.calender.integrationTests;

import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.mapper.Mapper;
import com.example.calender.repository.EmployeeRepository;
import com.example.calender.repository.OfficeRepository;
import com.example.calender.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @MockBean
    private RestTemplate restTemplate;

    public Employee initEmployee(Office office){

        Employee testEmp = Employee.builder()
                .name("jackson")
                .email("mail@email.com")
                .office(office)
                .houseAddress("manglore")
                .mob("8296336917")
                .build();
        return testEmp;
    }

    public Office initOffice(){
        Office office=Office.builder()
                .location("banglore")
                .build();
        return office;
    }


    @Test
    public void getAllEmployees_WhenReqSent_ThenEmployeesParametersMatch() throws Exception {

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=employeeRepository.save(employee);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email", CoreMatchers.is(savedEmployee.getEmail())));

    }

    @Test
    public void getEmployee_WhenValidID_ThenReturnEmployee()throws Exception{

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=employeeRepository.save(employee);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/"+savedEmployee.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(savedEmployee.getEmail())));

    }

    @Test
    public void getEmployee_WhenInValidID_ThenStatusCodeMatch()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=employeeRepository.save(employee);

        int invalidId=1001;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/"+invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public  void getEmployeeByIdOrEmail_WhenValidId_ThenEmployeeDetailsMatch()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=employeeRepository.save(employee);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee")
                        .param("id",savedEmployee.getId()+"")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(savedEmployee.getEmail())));

    }


    @Test
    public  void getEmployeeByIdOrEmail_WhenValidEmail_ThenEmployeeDetailsMatch()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=employeeRepository.save(employee);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee")
                        .param("email",savedEmployee.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(savedEmployee.getEmail())));

    }

    @Test
    public  void getEmployeeByIdOrEmail_WhenNoParameters_ThenStatusCodeMatch()throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void insertEmployee_WhenValidEmployee_ThenSaveEmployee()throws Exception{

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);
        Employee employee=initEmployee(savedOffice);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

        Employee savedEmployee=employeeRepository.findByEmail(employee.getEmail()).orElseThrow(()->new ResourceNotFoundException("employee","email",employee.getEmail()));
        assertEquals(employee.getEmail(),savedEmployee.getEmail());

    }

    @Test
    public void insertEmployee_WhenInValidOfficeID_ThenThrowNotFoundStatusCode()throws Exception{

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);

        Employee employee=initEmployee(savedOffice);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(HttpClientErrorException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());

       assertTrue(employeeRepository.findByEmail(employee.getEmail()).isEmpty());
    }


    @Test
    public  void deleteEmployee_WhenValidId_ThenDeleteEmployee()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);

        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=employeeRepository.save(employee);


        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee")
                        .param("id",employee.getId()+"")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        assertTrue(employeeRepository.findById(savedEmployee.getId()).isEmpty());

    }


    @Test
    public  void deleteEmployee_WhenValidEmail_ThenDeleteEmployee()throws Exception{
        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);

        Employee employee=initEmployee(savedOffice);
        Employee savedEmployee=employeeRepository.save(employee);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee")
                        .param("email",employee.getEmail()+"")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        assertTrue(employeeRepository.findById(savedEmployee.getId()).isEmpty());

    }

    @Test
    public void updateEmployee_WhenValidEmployee_ThenUpdateEmployee()throws Exception{

        Office office=initOffice();
        Office  savedOffice= officeRepository.save(office);

        Employee employee=initEmployee(savedOffice);
        employee.setId(100L);
        Employee savedEmployee=employeeRepository.save(employee);
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);

     employee.setName("Jammy");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/"+savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employee))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(employee.getName())));

        assertNotEquals(savedEmployee.getName(),employeeRepository.findById(savedOffice.getId()).get().getName());

    }




}
