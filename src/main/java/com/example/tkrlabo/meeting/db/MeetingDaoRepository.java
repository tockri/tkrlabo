package com.example.tkrlabo.meeting.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
interface MeetingDaoRepository extends CrudRepository<MeetingDao, Long> {

    @Query("""
            SELECT DISTINCT m
            FROM MeetingDao m
            WHERE m.id IN (
                SELECT ab.meetingId
                FROM MeetingAttendeeBankerDao ab
                WHERE ab.bankerId = :bankerId
            )
            AND m.createdAt >= :fromDate
            """)
    List<MeetingDao> findByBankerIdAndFromDate(Long bankerId, LocalDateTime fromDate);
}