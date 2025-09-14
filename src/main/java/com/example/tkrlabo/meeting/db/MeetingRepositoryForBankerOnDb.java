package com.example.tkrlabo.meeting.db;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.example.tkrlabo.meeting.domain.MeetingRepositoryForBanker;
import com.example.tkrlabo.meeting.domain.dto.MeetingInput;
import com.example.tkrlabo.meeting.domain.entity.Meeting;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Component
@Validated
@RequiredArgsConstructor
public class MeetingRepositoryForBankerOnDb implements MeetingRepositoryForBanker {
    private final MeetingDaoRepository meetingDaoRepository;
    private final MeetingAttendeeBankerDaoRepository meetingAttendeeBankerRepository;
    private final MeetingAttendeeUserDaoRepository meetingAttendeeUserRepository;

    @Override
    public List<Meeting> findByBankerId(Long bankerId, LocalDate fromDate) {
        var fromDateTime = fromDate.atStartOfDay();
        var meetings = meetingDaoRepository.findByBankerIdAndFromDate(bankerId, fromDateTime);
        if (meetings.isEmpty()) {
            return Collections.emptyList();
        }
        var meetingIds = meetings.stream().map(MeetingDao::getId).toList();
        var attendeeBankers = meetingAttendeeBankerRepository.findByMeetingIds(meetingIds);
        var attendeeUsers = meetingAttendeeUserRepository.findByMeetingIds(meetingIds);
        return DaoConverter.convertList(meetings, attendeeBankers, attendeeUsers);
    }

    @Override
    public void insert(@Valid MeetingInput input) {
        var daoSet = DtoConverter.convertInput(input);
        var meeting = daoSet.meeting();
        var attendeeUsers = daoSet.attendeeUsers();
        var attendeeBankers = daoSet.attendeeBankers();

        var saved = meetingDaoRepository.save(meeting);
        attendeeUsers.forEach(ud -> ud.setMeetingId(saved.getId()));
        meetingAttendeeUserRepository.saveAll(attendeeUsers);
        attendeeBankers.forEach(ab -> ab.setMeetingId(saved.getId()));
        meetingAttendeeBankerRepository.saveAll(attendeeBankers);
    }
}
