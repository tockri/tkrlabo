package com.example.tkrlabo.meeting.domain;

import java.time.LocalDate;
import java.util.List;

import com.example.tkrlabo.meeting.domain.entity.Meeting;
import com.example.tkrlabo.meeting.domain.entity.MeetingInput;

public interface MeetingRepositoryForBanker {
    List<Meeting> findByBankerId(Long bankerId, LocalDate fromDate);

    void insert(MeetingInput input);
}
