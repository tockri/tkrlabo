package com.example.tkrlabo.meeting.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingAttendeeBankerRepository extends CrudRepository<MeetingAttendeeBankerDao, Long> {
}