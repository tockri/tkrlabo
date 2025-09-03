package com.example.tkrlabo.meeting.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.lang.NonNull;

public record Meeting(@NonNull Long id, @NonNull String msEventId,
            @NonNull String subject, @NonNull String creator,
            @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt,
            @NonNull List<MeetingAttendeeUser> attendeeUsers,
            @NonNull List<MeetingAttendeeBanker> attendeeBankers) {
}
