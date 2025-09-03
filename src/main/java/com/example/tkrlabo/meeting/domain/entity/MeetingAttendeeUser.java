package com.example.tkrlabo.meeting.domain.entity;

import org.springframework.lang.NonNull;

public record MeetingAttendeeUser(@NonNull Long id, @NonNull Long userId) {
}
