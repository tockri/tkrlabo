package com.example.tkrlabo.meeting.db;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MeetingAttendeeUserDaoRepository extends CrudRepository<MeetingAttendeeUserDao, Long> {
    @Query("SELECT au.* FROM meeting_attendee_user au WHERE au.meeting_id IN (:meetingIds)")
    List<MeetingAttendeeUserDao> findByMeetingIdsInternal(Collection<Long> meetingIds);

    default List<MeetingAttendeeUserDao> findByMeetingIds(Collection<Long> meetingIds) {
        if (meetingIds == null || meetingIds.isEmpty()) {
            return List.of();
        }
        return findByMeetingIdsInternal(meetingIds);
    }
}
