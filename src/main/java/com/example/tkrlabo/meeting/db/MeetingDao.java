package com.example.tkrlabo.meeting.db;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "meeting")
@AllArgsConstructor(staticName = "of")
public class MeetingDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ms_event_id")
    private String msEventId;
    
    private String subject;

    private String creator;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
