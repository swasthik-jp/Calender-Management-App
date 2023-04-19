package com.example.calender.mapper;
import com.example.calender.dto.MeetingRoomDto;
import com.example.calender.dto.OfficeDto;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.entity.Office;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MeetingRoomMapperTest {


    @Mock
    Mapper<Office, OfficeDto> officeDToMapper;

    @InjectMocks
    MeetingRoomMapper meetingRoomMapper;


    @Test
    public void toDto_WhenValidMeetingRoom_ThenParametersMatch() {
        Office office=Office.builder()
                .id(2L)
                .location("new york")
                .build();
        MeetingRoom meetingRoom=MeetingRoom.builder()
                .id(1L)
                .name("meetingRoom1")
                .capacity(1)
                .isOperational(true)
                .office(office).
                build();

        OfficeDto officeDto=OfficeDto.builder()
                .id(2L)
                .location(office.getLocation())
                .build();


        when(officeDToMapper.toDto(office)) .thenReturn(officeDto);
        MeetingRoomDto meetingRoomDto= meetingRoomMapper.toDto(meetingRoom);
        assertEquals(meetingRoom.getId(),meetingRoomDto.getId());
        assertEquals(meetingRoom.getName(),meetingRoomDto.getName());
        assertEquals(office.getId(),meetingRoomDto.getOffice().getId());
    }

    @Test
    public void toEntity_WhenValidMeetingRoomDtoWithNullID_ThenParametersMatch() {
        Office office=Office.builder()
                .location("new york")
                .build();

        OfficeDto officeDto=OfficeDto.builder()
                .location(office.getLocation())
                .build();

        MeetingRoomDto meetingRoomDto=MeetingRoomDto.builder()
                .name("meetingRoom1")
                .capacity(1)
                .isOperational(true)
                .office(officeDto).
                build();

        when(officeDToMapper.toEntity(officeDto)) .thenReturn(office);
        MeetingRoom meetingRoom= meetingRoomMapper.toEntity(meetingRoomDto);
        assertEquals(null,meetingRoom.getId());
        assertEquals(meetingRoomDto.getName(),meetingRoom.getName());
        assertEquals(null,meetingRoom.getOffice().getId());
    }

    @Test
    public void toEntity_WhenValidMeetingRoomDtoWithValidID_ThenIDIsNull() {
        Office office=Office.builder()
                .location("new york")
                .build();

        OfficeDto officeDto=OfficeDto.builder()
                .location(office.getLocation())
                .build();

        MeetingRoomDto meetingRoomDto=MeetingRoomDto.builder()
                .name("meetingRoom1")
                .capacity(1)
                .isOperational(true)
                .office(officeDto).
                build();

        when(officeDToMapper.toEntity(officeDto)) .thenReturn(office);
        MeetingRoom meetingRoom= meetingRoomMapper.toEntity(meetingRoomDto);
        assertEquals(null,meetingRoom.getId());
    }


}