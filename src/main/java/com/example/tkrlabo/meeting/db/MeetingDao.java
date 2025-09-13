package com.example.tkrlabo.meeting.db;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "meeting")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MeetingDao {
    @Id
    private Long id;

    @NonNull
    @Column("ms_event_id")
    private String msEventId;

    @NonNull
    private String subject;

    @NonNull
    private String creator;

    @NonNull
    @Column("created_at")
    private LocalDateTime createdAt;

    @NonNull
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
