package com.example.tkrlabo.meeting.db;

import java.time.LocalDateTime;
import java.util.List;

import com.example.tkrlabo.meeting.domain.entity.MeetingAttendeeBankerInput;
import com.example.tkrlabo.meeting.domain.entity.MeetingAttendeeUserInput;
import com.example.tkrlabo.meeting.domain.entity.MeetingInput;

final class EntityConverter {
    public record DaoSet(MeetingDao meeting, List<MeetingAttendeeUserDao> attendeeUsers,
            List<MeetingAttendeeBankerDao> attendeeBankers) {
    }

    private static MeetingAttendeeUserDao convert(MeetingAttendeeUserInput input) {
        return MeetingAttendeeUserDao.of(null, input.userId(), null);
    }

    private static MeetingAttendeeBankerDao convert(MeetingAttendeeBankerInput input) {
        return MeetingAttendeeBankerDao.of(null, input.bankerId(), input.bankerName(), null);
    }

    public static DaoSet convertInput(MeetingInput input) {
        var attendeeUsers = input.attendeeUsers().stream()
                .map(EntityConverter::convert)
                .toList();
        var attendeeBankers = input.attendeeBankers().stream()
                .map(EntityConverter::convert)
                .toList();
        var dao = MeetingDao.of(null, input.msEventId(), input.subject(), input.creator(),
                LocalDateTime.now(), LocalDateTime.now());

        return new DaoSet(dao, attendeeUsers, attendeeBankers);
    }
}
