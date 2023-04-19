package com.example.calender.mapper;

import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.Office;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OfficeMapperTest {

    OfficeMapper officeMapper;

    @Test
    public void toDto_WhenValidOffice_ThenParametersMatch() {
        officeMapper=new OfficeMapper();
        Office office=Office.builder()
                .id(2L)
                .location("new york")
                .build();

        OfficeDto officeDto= officeMapper.toDto(office);
        assertEquals(officeDto.getId(),office.getId());
        assertEquals(officeDto.getLocation(),officeDto.getLocation());
    }

    @Test
    public void toEntity_WhenValidOfficeDtoWithNullID_ThenParametersMatch() {
        officeMapper=new OfficeMapper();

        OfficeDto officeDto=OfficeDto.builder()
                .location("New York")
                .build();

        Office office= officeMapper.toEntity(officeDto);
        assertEquals(null,office.getId());
        assertEquals(officeDto.getLocation(),office.getLocation());
    }

    @Test
    public void toEntity_WhenValidOfficeDtoWithValidID_ThenIDMatches() {
        officeMapper=new OfficeMapper();
        OfficeDto officeDto=OfficeDto.builder()
                .id(1L)
                .location("New York")
                .build();

        Office office= officeMapper.toEntity(officeDto);
        assertEquals(officeDto.getId(),office.getId());
    }

}