package com.example.tkrlabo.meeting.domain;

import java.time.LocalDate;
import java.util.List;

import com.example.tkrlabo.meeting.domain.dto.MeetingInput;
import com.example.tkrlabo.meeting.domain.entity.Meeting;

import jakarta.validation.Valid;

public interface MeetingRepositoryForBanker {
    List<Meeting> findByBankerId(Long bankerId, LocalDate fromDate);

    void insert(@Valid MeetingInput input);
}
