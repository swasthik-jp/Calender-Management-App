package com.example.calender.service;

import com.example.calender.constants.AttendingStatus;
import com.example.calender.constants.MeetingStatus;
import com.example.calender.entity.Attendee;
import com.example.calender.entity.Meeting;
import com.example.calender.entity.MeetingRoom;
import com.example.calender.exception.PolicyViolationException;
import com.example.calender.exception.ResourceNotFoundException;
import com.example.calender.repository.MeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AttendeeService attendeeService;

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Boolean canSchedule(Set<String> attendees, Date start, Date end)
            throws PolicyViolationException, ResourceNotFoundException {
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        long timeInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        Date now = new Date();

        if (attendees.size() > 5 || timeInMinutes < 30)
            throw new PolicyViolationException("A");

        int startHour = Integer.parseInt(start.toString().split(" ")[3].split(":")[0]);
        int endHour = Integer.parseInt(end.toString().split(" ")[3].split(":")[0]);

        if (startHour < 10 || endHour >= 18)
            throw new PolicyViolationException("B");

        if (end.before(start))
            throw new IllegalArgumentException("End Date Cannot be before Start Date!");

        if (start.before(now))
            throw new IllegalArgumentException("Can't schedule meeting in `Past`!");

        Optional<List<List<Long>>> allMeetings = meetingRepository.getAllMeetingAndRoomIdForGivenDateRange(start, end);

        if (allMeetings.isPresent()) {
            Set<Long> uniqueRoomsIds = allMeetings.get().stream()
                    .map(pairs -> pairs.get(1))
                    .collect(Collectors.toSet());
            List<MeetingRoom> allMeetingRooms = meetingRoomService.getAllMeetingRooms();

            if (uniqueRoomsIds.size() == allMeetingRooms.size()) { // if there is no meeting room available (all are occupied)
                return false;
            }
            attendees.forEach(email -> employeeService.getEmployeeByEmail(email));
//            for (Pair<Long, Long> meetId : allMeetings.get()) {
//                Meeting meeting = meetingRepository.findById(meetId.getFirst()).orElseThrow(() -> new ResourceNotFoundException("Meeting", "id", meetId.getFirst()));
//                for (Attendee meetAttendee : meeting.getAttendees()) {
//                    if (meetAttendee.getIsAttending() == AttendingStatus.YES) {
//                        return false;
//                    }
//                }
//            }
            return true;
        } else {
            return meetingRoomService.getAllMeetingRooms().isEmpty();
        }
    }

    @Override
    public Long scheduleMeeting(Meeting meeting) {
        //call function can schedule and proceed if fine

        Set<String> employeeEmail = meeting.getAttendees().stream()
                .map(employee -> employee.getEmployee().getEmail())
                .collect(Collectors.toSet());

        Date start = meeting.getStartTimeStamp();
        Date end = meeting.getEndTimeStamp();

        if (Boolean.FALSE.equals(
                canSchedule(employeeEmail, start, end)
        ))
            return null;

        /*
        Check valid Room or Not
        At this point it has already been determined there exist atleast one available room
         */

        Optional<List<List<Long>>> allMeetingsInGivenRange = meetingRepository.getAllMeetingAndRoomIdForGivenDateRange(start, end);
        if (allMeetingsInGivenRange.isPresent()) {
            Set<Long> allOccupiedRoomIdSet = allMeetingsInGivenRange.get().stream()
                    .map(pairs -> pairs.get(1))
                    .collect(Collectors.toSet());

            List<MeetingRoom> allMeetingRooms = meetingRoomService.getAllMeetingRooms();
            Set<Long> allRoomIdOnlySet = allMeetingRooms.stream()
                    .filter(MeetingRoom::isOperational)
                    .map(MeetingRoom::getId)
                    .collect(Collectors.toSet());
            allRoomIdOnlySet.removeAll(allOccupiedRoomIdSet);
            // At this point allRoomIdSet should only contain available rooms

            if (meeting.getAllocatedRoom() == null) {

                List<Long> meetingRoomsInHostOffice = meetingRoomService.getMeetingRoomsByOfficeId(meeting.getHost().getOffice().getId());
                Set<Long> availableRooms = new HashSet<>(allRoomIdOnlySet);
                availableRooms.retainAll(meetingRoomsInHostOffice);
                if (!availableRooms.isEmpty())
                    meeting.setAllocatedRoom(meetingRoomService.getMeetingRoomById(availableRooms.iterator().next()));
                else
                    meeting.setAllocatedRoom(meetingRoomService.getMeetingRoomById(allRoomIdOnlySet.iterator().next()));

            } else {
                Long roomId = meeting.getAllocatedRoom().getId();
                if (!meeting.getAllocatedRoom().isOperational())
                    return null;
                if (!allRoomIdOnlySet.contains(roomId))
                    throw new ResourceNotFoundException("MeetingRoom", "id", roomId);
                meeting.setAllocatedRoom(meetingRoomService.getMeetingRoomById(roomId));
            }
        }

        /*
        Following block of code first saves attendees in database after they are verified
        then Set of Attendees is updated so that it now includes id of attendees
         */
        Set<Attendee> attendeesList = new HashSet<>();
        for (Attendee participant : meeting.getAttendees()) {
            participant.setIsAttending(AttendingStatus.PENDING);
            attendeesList.add(attendeeService.save(participant));
        }
        Attendee mainHost = Attendee.builder()
                .employee(meeting.getHost())
                .isAttending(AttendingStatus.YES)
                .build();
        attendeesList.add(attendeeService.save(mainHost));
        meeting.setAttendees(attendeesList);
        meeting.setStatus(MeetingStatus.PENDING);
        /*
        Finally meeting is saved
        PROBLEM TO TAKE CARE: Attendees should be saved only if meeting is saved
         */
        Meeting m = meetingRepository.save(meeting);
        return m.getId();
    }

    @Override
    public MeetingStatus changeMeetingStatus(Long id, MeetingStatus newStatus) {
        Optional<Meeting> meeting = meetingRepository.findById(id);
        if (meeting.isPresent()) {
            Meeting foundMeeting = meeting.get();
            foundMeeting.setStatus(newStatus);
            meetingRepository.save(foundMeeting);
            return foundMeeting.getStatus();
        } else {
            throw new ResourceNotFoundException("Meeting", "id", id);
        }
    }

    @Override
    public Meeting getMeetingDetails(Long id) {
        return meetingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Meeting.class.getSimpleName(),"id",id));
    }

    @Override
    public List<Meeting> getMeetingsInCustomRange(Date start, Date end) {
        return meetingRepository.getAllMeetingForCustomDateRange(start,end).orElseThrow(() -> new ResourceNotFoundException(Meeting.class.getSimpleName(),"filter",start.toString()+" to "+end.toString()));
    }

    @Override
    public List<Meeting> getMeetingsInParticularWeek(char sign, int byWeek) {
        if(byWeek == 0)
            return meetingRepository.getAllMeetingForCurrentWeek().orElseThrow(() -> new ResourceNotFoundException(Meeting.class.getSimpleName(),"filter","CURRENT_WEEK"));

        if(sign == '+')
            return meetingRepository.getAllMeetingForNextParticularWeek(byWeek).orElseThrow(() -> new ResourceNotFoundException(Meeting.class.getSimpleName(),"filter","Next "+byWeek+" Weeks"));
        else if(sign == '-')
            return meetingRepository.getAllMeetingForPastParticularWeek(byWeek).orElseThrow(() -> new ResourceNotFoundException(Meeting.class.getSimpleName(),"filter","Past "+byWeek+" Weeks"));
        else throw new IllegalArgumentException();
    }

    @Override
    public List<Meeting> getParticularEmployeeMeetings(List<Meeting> meetingsList, Long id) {
        List<Meeting> hisMeetings = new ArrayList<>();
        for(Meeting meeting: meetingsList)
            for(Attendee attendee: meeting.getAttendees())
                if(Objects.equals(attendee.getEmployee().getId(), id))
                {
                    hisMeetings.add(meeting);
                    break;
                }
        return hisMeetings;
    }

    @Override
    public AttendingStatus setAttendeeStatus(Long meetingId, Long employeeId, AttendingStatus attendingStatus) {
        Optional<Meeting> foundMeeting = meetingRepository.findById(meetingId);
        if (foundMeeting.isPresent()) {
            Meeting meeting = foundMeeting.get();
            Optional<Attendee> attendeeWithEmpId = meeting.getAttendees().stream()
                    .filter(attendee -> attendee.getEmployee().getId() == employeeId)
                    .findFirst();
            if (!attendeeWithEmpId.isPresent())
                throw new ResourceNotFoundException("Employee", "id", employeeId);
            attendeeWithEmpId.get().setIsAttending(attendingStatus);
            meetingRepository.save(meeting);
            return attendingStatus;
        } else {
            throw new ResourceNotFoundException("Meeting", "id", meetingId);
        }
    }


}
