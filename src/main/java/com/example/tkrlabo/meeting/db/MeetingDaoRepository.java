package com.example.tkrlabo.meeting.db;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
interface MeetingDaoRepository extends CrudRepository<MeetingDao, Long> {

    @Query("""
            SELECT m.id, m.ms_event_id, m.subject, m.creator, m.created_at, m.updated_at
            FROM meeting m
            WHERE m.id IN (
                SELECT ab.meeting_id
                FROM meeting_attendee_banker ab
                WHERE ab.banker_id = :bankerId
            )
            AND m.created_at >= :fromDate
            ORDER BY m.created_at DESC
            """)
    List<MeetingDao> findByBankerIdAndFromDate(Long bankerId, LocalDateTime fromDate);
}
