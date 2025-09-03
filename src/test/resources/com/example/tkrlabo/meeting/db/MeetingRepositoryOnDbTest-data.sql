-- テストデータの準備
-- Meeting 1: 参加者1名ずつ (user: 100, banker: 1001)
INSERT INTO meeting (ms_event_id, subject, creator, created_at, updated_at) 
VALUES ('event-1', 'Meeting 1', 'creator1', NOW(), NOW());

-- Meeting 2: 参加者2名ずつ (users: 200,300, bankers: 1001,1002)
-- ※ banker 1001は両方のミーティングに参加
INSERT INTO meeting (ms_event_id, subject, creator, created_at, updated_at) 
VALUES ('event-2', 'Meeting 2', 'creator2', NOW(), NOW());

-- Meeting 1の参加者
INSERT INTO meeting_attendee_user (user_id, meeting_id) 
SELECT 100, id FROM meeting WHERE ms_event_id = 'event-1';

INSERT INTO meeting_attendee_banker (banker_id, banker_name, meeting_id) 
SELECT 1001, 'Banker A', id FROM meeting WHERE ms_event_id = 'event-1';

-- Meeting 2の参加者
INSERT INTO meeting_attendee_user (user_id, meeting_id) 
SELECT 200, id FROM meeting WHERE ms_event_id = 'event-2'
UNION ALL
SELECT 300, id FROM meeting WHERE ms_event_id = 'event-2';

INSERT INTO meeting_attendee_banker (banker_id, banker_name, meeting_id) 
SELECT 1001, 'Banker A', id FROM meeting WHERE ms_event_id = 'event-2'
UNION ALL
SELECT 1002, 'Banker C', id FROM meeting WHERE ms_event_id = 'event-2';