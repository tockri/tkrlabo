package com.example.tkrlabo.meeting.db;

import org.springframework.lang.NonNull;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "meeting_attendee_banker")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MeetingAttendeeBankerDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "banker_id")
    private Long bankerId;

    @NonNull
    @Column(name = "banker_name")
    private String bankerName;

    @Column(name = "meeting_id")
    private Long meetingId;
}