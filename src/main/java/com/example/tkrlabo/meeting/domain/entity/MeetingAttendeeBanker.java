package com.example.tkrlabo.meeting.domain.entity;

import org.springframework.lang.NonNull;

public record MeetingAttendeeBanker(@NonNull Long id, @NonNull Long bankerId, @NonNull String bankerName) {
}
