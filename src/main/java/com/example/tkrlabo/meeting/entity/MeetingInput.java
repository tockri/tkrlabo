package com.example.tkrlabo.meeting.entity;

import java.util.List;

public record MeetingInput(String msEventId, String subject,String creator, 
                                List<MeetingAttendeeUserInput> attendeeUsers, 
                                List<MeetingAttendeeBankerInput> attendeeBankers) {
}
