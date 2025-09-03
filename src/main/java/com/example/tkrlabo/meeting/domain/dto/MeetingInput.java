package com.example.tkrlabo.meeting.domain.dto;

import java.util.List;

import org.springframework.lang.NonNull;

public record MeetingInput(@NonNull String msEventId, @NonNull String subject,
        @NonNull String creator,
        @NonNull List<MeetingAttendeeUserInput> attendeeUsers,
        @NonNull List<MeetingAttendeeBankerInput> attendeeBankers) {
}
