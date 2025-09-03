package com.example.tkrlabo.meeting.db;

import java.util.List;

import com.example.tkrlabo.meeting.domain.entity.Meeting;
import com.example.tkrlabo.meeting.domain.entity.MeetingAttendeeBanker;
import com.example.tkrlabo.meeting.domain.entity.MeetingAttendeeUser;

final class DaoConverter {
    private static MeetingAttendeeUser convert(MeetingAttendeeUserDao dao) {
        return new MeetingAttendeeUser(dao.getId(), dao.getUserId());
    }

    private static MeetingAttendeeBanker convert(MeetingAttendeeBankerDao dao) {
        return new MeetingAttendeeBanker(dao.getId(), dao.getBankerId(), dao.getBankerName());
    }

    public static List<Meeting> convertList(List<MeetingDao> meetings, List<MeetingAttendeeBankerDao> attendeeBankers,
            List<MeetingAttendeeUserDao> attendeeUsers) {
        return meetings.stream()
                .map(m -> {
                    var users = attendeeUsers.stream()
                            .filter(au -> au.getMeetingId().equals(m.getId()))
                            .map(DaoConverter::convert)
                            .toList();
                    var bankers = attendeeBankers.stream()
                            .filter(ab -> ab.getMeetingId().equals(m.getId()))
                            .map(DaoConverter::convert)
                            .toList();
                    return new Meeting(m.getId(), m.getMsEventId(), m.getSubject(), m.getCreator(),
                            m.getCreatedAt(), m.getUpdatedAt(), users, bankers);
                })
                .toList();
    }
}
