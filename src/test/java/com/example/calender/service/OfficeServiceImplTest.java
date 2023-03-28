package com.example.calender.service;

import com.example.calender.entity.Office;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.OfficeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfficeServiceImplTest {
    @Mock
    OfficeRepository officeRepository;

    @Mock
    EmployeeService employeeService;

    @Mock
    MeetingRoomService meetingRoomService;

    @InjectMocks
    OfficeService officeService = new OfficeServiceImpl();

    @Test
    public void when_getOfficeByIdIsCalled_thenExpectOfficeObject() throws ResourceNotFoundException {
        Office tempOffice = Office.builder()
                .id(Long.valueOf(10))
                .build();
        when(officeRepository.findById(anyLong())).thenReturn(Optional.of(tempOffice));
        long officeId = officeService.getOfficeById(10).getId();
        Assert.assertEquals(10, officeId);
    }

    @org.junit.Test(expected = ResourceNotFoundException.class)
    public void when_getOfficeByIdIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {
        when(officeRepository.findById(anyLong())).thenReturn(Optional.empty());
        officeService.getOfficeById(10);
    }

    @org.junit.Test(expected = ResourceNotFoundException.class)
    public void when_deleteOfficeByIdIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {
        when(officeRepository.findById(10L)).thenReturn(Optional.empty());
        officeService.deleteOffice(10);
    }

    @org.junit.Test
    public void when_deleteOfficeByIdIsCalled_thenExpectOfficeToBeDeleted() throws ResourceNotFoundException {
        when(officeRepository.findById(10L)).thenReturn(Optional.of(new Office()));
        when(employeeService.checkEmptyOffice(10L)).thenReturn(true);
        when(meetingRoomService.getMeetingRoomsByOfficeId(10L)).thenReturn(List.of(1L, 2L));
        Assert.assertTrue(officeService.deleteOffice(10));
    }

    @org.junit.Test
    public void when_deleteOfficeByIdIsCalled_thenExpectOfficeNotToBeDeleted() throws ResourceNotFoundException {
        when(officeRepository.findById(10L)).thenReturn(Optional.of(new Office()));
        when(employeeService.checkEmptyOffice(10L)).thenReturn(false);
        Assert.assertFalse(officeService.deleteOffice(10));
    }

    @org.junit.Test
    public void when_updateOfficeByIdIsCalled_thenExpectOfficeToBeUpdated() throws ResourceNotFoundException {
        Office tempOffice = Office.builder()
                .id(10L)
                .location("Bangalore")
                .build();
        when(officeRepository.findById(10L)).thenReturn(Optional.of(new Office()));
        Assert.assertEquals("Bangalore", officeService.updateOffice(tempOffice, 10L).getLocation());
    }
    @org.junit.Test(expected = ResourceNotFoundException.class)
    public void when_updateOfficeByIdIsCalled_thenExpectResourceNotFoundException() throws ResourceNotFoundException {

        when(officeRepository.findById(10L)).thenReturn(Optional.empty());
        officeService.updateOffice(null, 10L);
    }
}