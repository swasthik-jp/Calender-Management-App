# Calender-Management-App


## Problem Summary

Build a calendar management system to manage meetings of employees for a very large organization  XYZ having 5L+ employees, and with over 100 offices, each with roughly 10 meeting rooms across the country.

The system should have following features:

addEmployee -> Given a employee id, name, work email address, office location etc., add the employee to the system
removeEmployee -> Given a work email address or employee id, remove the employee from the system
addMeetingRoom -> Given a meeting room name, office location, add the meeting room to the system and return meetingRoomId
disable/enable MeetingRoom -> Given a meeting room name or meetingRoomId, disable/enable the room
canSchedule -> Given a list of employee ids, meeting start and end, should check whether the meeting is possible or not.
scheduleMeeting -> description, agenda and input to above feature will be input to this feature , should block the calendars of the given employees and assign any available meeting room and return a meetingId. If a meeting room is provided in input, that room should be used instead. Both this and above features should respect below policies of XYZ
XYZ believes any meeting with more than 6 people or not having agenda or less than 30 min duration won’t be productive
XYZ respects work life balance of employees and discourage meetings outside working hours (10AM to 6PM).
This method can be made asynchronous, we should fire and forget, Meeting scheduling can happen asynchronously, We also need to ensure that no meetings should be lost once reached here.
cancelMeeting -> Given a meetingId, should cancel the meeting
accept/Decline Meeting -> Given a meetingId, employeeId, set status to accepted/declined for that employee
getMeetingRoomInfo -> Given a meeting room id, returns meeting room details
getMeetings -> Given a employeeId, returns all meetings for that day by default, other options should be accepted are current_week, last_week and any custom date ranges
getMeetingDetails -> Given a meetingId, should return all details of the meeting, like meeting owner, meeting room, all employees involved with their status etc.


## Technical Constraints
Design a micro-service based architecture which has one API (Rest) endpoint layer and a Thrift endpoint layer which is a separate microservice.
Rest (HTTP) Endpoint: All feature requests shall be served from this service and it should manage all data except meetings
Thrift  Endpoint for meetings: All meeting related data should be owned by this service. Read how to create a thrift endpoint here (https://livebook.manning.com/book/programmers-guide-to-apache-thrift/chapter-1/). 






Decide and implement logging framework with pattern and request_id, Request_id to be present in all log lines for the same request.
Response times to fetch for all get calls should be lightening fast, 95% below 5ms for 50 rps.


## Milestones to be tracked
Create entity relationship diagram.
Create DB schema and all the tables in MYSQL/Mongo, define indexes correctly, get reviewed if needed, keep separate database for HTTP and Thrift endpoints.
Define all the API endpoints which you will expose, Show the signatures in postman.
Create API end to end using TDD.
Add Integration test on this API with atleast one success case as well as failure case.
All integration test should mock the thrift service.
Check API response time, See if response times can be optimised.
Ensure code coverage is > 90%.
Define different monitorings and alerts which you will be configuring. 
Final demo to all stakeholders.
