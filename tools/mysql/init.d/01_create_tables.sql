-- Database schema for tkrlabo
-- Generated from docs/db.er.md

-- Meeting table
CREATE TABLE meeting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ms_event_id VARCHAR(255),
    subject TEXT,
    creator VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Meeting attendee user table
CREATE TABLE meeting_attendee_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE
);

-- Meeting attendee banker table
CREATE TABLE meeting_attendee_banker (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_id BIGINT NOT NULL,
    banker_id BIGINT NOT NULL,
    banker_name VARCHAR(255),
    FOREIGN KEY (meeting_id) REFERENCES meeting(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_meeting_attendee_user_meeting_id ON meeting_attendee_user(meeting_id);
CREATE INDEX idx_meeting_attendee_banker_meeting_id ON meeting_attendee_banker(meeting_id);
