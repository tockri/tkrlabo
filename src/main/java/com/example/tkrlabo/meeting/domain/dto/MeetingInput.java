package com.example.tkrlabo.meeting.domain.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MeetingInput(
        @NotBlank String msEventId,
        @NotBlank String subject,
        @NotBlank String creator,
        @Valid @NotEmpty List<MeetingInput.AttendeeUser> attendeeUsers,
        @Valid @NotEmpty List<MeetingInput.AttendeeBanker> attendeeBankers
) {
    public static record AttendeeUser(@NotNull Long userId) {}
    public static record AttendeeBanker(@NotNull Long bankerId, @NotBlank String bankerName) {}
}
