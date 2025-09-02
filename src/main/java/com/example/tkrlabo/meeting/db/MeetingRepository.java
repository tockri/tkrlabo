package com.example.tkrlabo.meeting.db;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.tkrlabo.meeting.entity.Meeting;
import com.example.tkrlabo.meeting.entity.MeetingInput;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MeetingRepository {
    private final MeetingDaoRepository meetingDaoRepository;
    private final MeetingAttendeeBankerDaoRepository meetingAttendeeBankerRepository;
    private final MeetingAttendeeUserDaoRepository meetingAttendeeUserRepository;

    public List<Meeting> findByBankerId(Long bankerId, LocalDate fromDate) {
        var meetings = meetingDaoRepository.findByBankerIdAndFromDate(bankerId, fromDate);
        var meetingIds = meetings.stream().map(MeetingDao::getId).toList();
        var attendeeBankers = meetingAttendeeBankerRepository.findByMeetingIds(meetingIds);
        var attendeeUsers = meetingAttendeeUserRepository.findByMeetingIds(meetingIds);
        return DaoConverter.convertList(meetings, attendeeBankers, attendeeUsers);
    }

    public void insert(MeetingInput input) {
        var daoSet = EntityConverter.convertInput(input);
        var attendeeBankers = daoSet.attendeeBankers();
        var attendeeUsers = daoSet.attendeeUsers();
        var saved = meetingDaoRepository.save(daoSet.meeting());
        attendeeUsers.forEach(ud -> ud.setMeetingId(saved.getId()));
        meetingAttendeeUserRepository.saveAll(attendeeUsers);
        attendeeBankers.forEach(ab -> ab.setMeetingId(saved.getId()));
        meetingAttendeeBankerRepository.saveAll(attendeeBankers);
    }
}
