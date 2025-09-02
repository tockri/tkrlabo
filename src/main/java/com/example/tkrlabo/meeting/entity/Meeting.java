package com.example.tkrlabo.meeting.entity;

import java.time.LocalDateTime;
import java.util.List;

public record Meeting(Long id, String msEventId, String subject, 
                    String creator, 
                    LocalDateTime createdAt, LocalDateTime updatedAt,
                       List<MeetingAttendeeUser> attendeeUsers, 
                       List<MeetingAttendeeBanker> attendeeBankers) {
}
