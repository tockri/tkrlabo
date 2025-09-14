-- テストデータの準備
-- クリーンアップ（テスト毎に実行されるため重複回避）
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE meeting_attendee_user;
TRUNCATE TABLE meeting_attendee_banker;
TRUNCATE TABLE meeting;
SET FOREIGN_KEY_CHECKS = 1;
-- Meeting 1: 参加者1名ずつ (user: 100, banker: 1001)
INSERT INTO meeting (
        id,
        ms_event_id,
        subject,
        creator,
        created_at,
        updated_at
    )
VALUES (
        10001,
        'event-1',
        'Meeting 1',
        'creator1',
        '2024-01-01 10:00:00',
        '2024-01-01 10:00:00'
    );
-- Meeting 2: 参加者2名ずつ (users: 200,300, bankers: 1001,1002)
-- ※ banker 1001は両方のミーティングに参加
INSERT INTO meeting (
        id,
        ms_event_id,
        subject,
        creator,
        created_at,
        updated_at
    )
VALUES (
        10002,
        'event-2',
        'Meeting 2',
        'creator2',
        '2024-01-01 10:00:00',
        '2024-01-01 10:00:00'
    );
--
-- Meeting 1の参加者
INSERT INTO meeting_attendee_user (user_id, meeting_id)
VALUES (100, 10001);
INSERT INTO meeting_attendee_banker (banker_id, banker_name, meeting_id)
VALUES (1001, 'Banker A', 10001);
--
-- Meeting 2の参加者
INSERT INTO meeting_attendee_user (user_id, meeting_id)
VALUES (101, 10002),
    (102, 10002);
INSERT INTO meeting_attendee_banker (banker_id, banker_name, meeting_id)
VALUES (1001, 'Banker A', 10002),
    (1002, 'Banker C', 10002);