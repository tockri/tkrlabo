package com.example.tkrlabo.meeting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "meeting_attendee_user")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MeetingAttendeeUserDao {
    @Id
    private Long id;

    @NonNull
    @Column("user_id")
    private Long userId;

    @Column("meeting_id")
    private Long meetingId;
}
