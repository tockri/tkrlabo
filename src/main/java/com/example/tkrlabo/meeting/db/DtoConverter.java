package com.example.tkrlabo.meeting.db;

import java.time.LocalDateTime;
import java.util.List;

import com.example.tkrlabo.meeting.domain.dto.MeetingInput;

final class DtoConverter {
    public record DaoSet(MeetingDao meeting, List<MeetingAttendeeUserDao> attendeeUsers,
            List<MeetingAttendeeBankerDao> attendeeBankers) {
    }

    private static MeetingAttendeeUserDao convert(MeetingInput.AttendeeUser input) {
        return MeetingAttendeeUserDao.of(null, input.userId(), null);
    }

    private static MeetingAttendeeBankerDao convert(MeetingInput.AttendeeBanker input) {
        return MeetingAttendeeBankerDao.of(null, input.bankerId(), input.bankerName(), null);
    }

    public static DaoSet convertInput(MeetingInput input) {
        var attendeeUsers = input.attendeeUsers().stream()
                .map(DtoConverter::convert)
                .toList();
        var attendeeBankers = input.attendeeBankers().stream()
                .map(DtoConverter::convert)
                .toList();
        var dao = MeetingDao.of(null, input.msEventId(), input.subject(), input.creator(),
                LocalDateTime.now(), LocalDateTime.now());

        return new DaoSet(dao, attendeeUsers, attendeeBankers);
    }
}
