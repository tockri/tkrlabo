package com.example.tkrlabo.meeting.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends CrudRepository<MeetingDao, Long> {
    
    @Query("SELECT DISTINCT m FROM MeetingDao m JOIN m.attendeeBankers ab WHERE ab.bankerId = :bankerId")
    List<MeetingDao> findByBankerId(@Param("bankerId") Long bankerId);
}