package com.example.tkrlabo.meeting.db;

import org.springframework.lang.NonNull;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "meeting_attendee_user")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MeetingAttendeeUserDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "meeting_id")
    private Long meetingId;
}