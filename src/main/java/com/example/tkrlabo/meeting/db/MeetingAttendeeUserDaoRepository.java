package com.example.tkrlabo.meeting.db;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MeetingAttendeeUserDaoRepository extends CrudRepository<MeetingAttendeeUserDao, Long> {
    @Query("SELECT au FROM MeetingAttendeeUserDao au WHERE au.meetingId IN :meetingIds")
    List<MeetingAttendeeUserDao> findByMeetingIds(Collection<Long> meetingIds);
}