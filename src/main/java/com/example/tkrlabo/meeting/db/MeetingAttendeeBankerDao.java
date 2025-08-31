package com.example.tkrlabo.meeting.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "meeting_attendee_banker")
@AllArgsConstructor(staticName = "of")
public class MeetingAttendeeBankerDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "banker_id")
    private Long bankerId;
    
    @Column(name = "banker_name")
    private String bankerName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private MeetingDao meeting;
}