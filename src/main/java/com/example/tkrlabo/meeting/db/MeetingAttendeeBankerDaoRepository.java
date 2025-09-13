package com.example.tkrlabo.meeting.db;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MeetingAttendeeBankerDaoRepository extends CrudRepository<MeetingAttendeeBankerDao, Long> {

    @Query("SELECT ab.* FROM meeting_attendee_banker ab WHERE ab.meeting_id IN (:meetingIds)")
    List<MeetingAttendeeBankerDao> findByMeetingIds(Collection<Long> meetingIds);
}
