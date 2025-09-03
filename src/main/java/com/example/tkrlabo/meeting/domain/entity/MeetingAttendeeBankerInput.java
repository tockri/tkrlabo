package com.example.tkrlabo.meeting.domain.entity;

import org.springframework.lang.NonNull;

public record MeetingAttendeeBankerInput(@NonNull Long bankerId, @NonNull String bankerName) {
}
