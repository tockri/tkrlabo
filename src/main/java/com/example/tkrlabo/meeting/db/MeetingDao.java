package com.example.tkrlabo.meeting.db;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "meeting")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MeetingDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "ms_event_id")
    private String msEventId;

    @NonNull
    private String subject;

    @NonNull
    private String creator;

    @NonNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NonNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
