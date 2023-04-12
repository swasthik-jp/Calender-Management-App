package com.example.calender.controller;

import com.example.calender.dto.EmployeeDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Employee;
import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.mapper.EmployeeMapper;
import com.example.calender.mapper.Mapper;
import com.example.calender.service.EmployeeService;
import com.example.calender.service.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.mapping.Any;
import org.hibernate.mapping.ManyToOne;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(webClientEnabled = false,addFilters  = false)
public class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
   private EmployeeService employeeService;

    @MockBean
   private Mapper<Employee, EmployeeDto> employeeDtoMapper;

    @MockBean
    private RestTemplate restTemplate;


    public Employee initEmployee(){
        Office office=Office.builder()
                .id(1L)
                .build();
        Employee testEmp = Employee.builder()
                .id(10L)
                .name("employee1")
                .email("mail@email.com")
                .office(office)
                .build();
        return testEmp;
    }

    public EmployeeDto initEmployeeDto(){

        OfficeDto officeDto=OfficeDto.builder()
                .id(1L)
                .build();

        EmployeeDto employeeDto=EmployeeDto.builder()
                .name("employee1")
                .email("mail@email.com")
                .office(officeDto)
                .build();
        return employeeDto;
    }

   @Test
   public void getAllEmployees_WhenReqSent_ThenEmployeesParametersMatch() throws Exception {
        Mockito.when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(initEmployee()));
        Mockito.when(employeeDtoMapper.toDto(initEmployee())).thenReturn(initEmployeeDto());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email", CoreMatchers.is(initEmployee().getEmail())));

    }

    @Test
    public void getEmployee_WhenValidID_ThenReturnEmployee()throws Exception{
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.when(employeeService.getEmployeeById(employee.getId())).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/"+employee.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

    }



    @Test
    public void getEmployee_WhenInValidID_ThenStatusCodeMatch()throws Exception{
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.when(employeeService.getEmployeeById(Mockito.anyLong())).thenThrow(ResourceNotFoundException.class);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        int invalidId=1001;
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/"+invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public  void getEmployeeByIdOrEmail_WhenValidId_ThenEmployeeDetailsMatch()throws Exception{
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.when(employeeService.getEmployeeById(employee.getId())).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee")
                        .param("id",employee.getId()+"")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

    }

    @Test
    public  void getEmployeeByIdOrEmail_WhenValidEmail_ThenEmployeeDetailsMatch()throws Exception{
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.when(employeeService.getEmployeeByEmail(employee.getEmail())).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee")
                        .param("email",employee.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

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
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);
        Mockito.when(employeeService.saveEmployee(employee)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toEntity(employeeDto)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(initEmployee()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

    }



    @Test
    public void insertEmployee_WhenInValidOfficeID_ThenThrowNotFoundStatusCode()throws Exception{
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(HttpClientErrorException.class);
        Mockito.when(employeeService.saveEmployee(employee)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toEntity(employeeDto)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(initEmployee()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public  void deleteEmployee_WhenValidId_ThenDeleteEmployee()throws Exception{
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.doNothing().when(employeeService).deleteEmployeeById(employee.getId());
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee")
                        .param("id",employee.getId()+"")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @Test
    public  void deleteEmployee_WhenValidEmail_ThenDeleteEmployee()throws Exception{
        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();
        Mockito.doNothing().when(employeeService).deleteEmployeeByEmail(employee.getEmail());
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee")
                        .param("email",employee.getEmail()+"")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @Test
    public  void deleteEmployee_WhenNoParameters_ThenStatusCodeMatch()throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void updateEmployee_WhenValidEmployee_ThenUpdateEmployee()throws Exception{

        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(OfficeDto.class);
        Mockito.when(employeeService.updateEmployee(employee,employee.getId())).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toEntity(employeeDto)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/"+employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employeeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

    }

    @Test
    public void updateEmployee_WhenInValidOfficeID_ThenThrowNotFoundStatusCode()throws Exception{

        Employee employee=initEmployee();
        EmployeeDto employeeDto=initEmployeeDto();

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(HttpClientErrorException.class);
        Mockito.when(employeeService.updateEmployee(employee,employee.getId())).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toEntity(employeeDto)).thenReturn(employee);
        Mockito.when(employeeDtoMapper.toDto(employee)).thenReturn(employeeDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/employee/"+employee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(employeeDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

}