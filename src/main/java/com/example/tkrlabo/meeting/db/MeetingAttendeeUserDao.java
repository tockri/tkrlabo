package com.example.tkrlabo.meeting.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "meeting_attendee_user")
@AllArgsConstructor(staticName = "of")
public class MeetingAttendeeUserDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "meeting_id")
    private Long meetingId;
}