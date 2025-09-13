-- Sample data for tkrlabo
SET NAMES 'utf8mb4';
-- Insert 10 meetings
INSERT INTO meeting (id, ms_event_id, subject, creator)
VALUES (1, 'ms_evt_0001', '打合せ 1', 'creator1'),
    (2, 'ms_evt_0002', '打合せ 2', 'creator2'),
    (3, 'ms_evt_0003', '打合せ 3', 'creator3'),
    (4, 'ms_evt_0004', '打合せ 4', 'creator4'),
    (5, 'ms_evt_0005', '打合せ 5', 'creator5'),
    (6, 'ms_evt_0006', '打合せ 6', 'creator6'),
    (7, 'ms_evt_0007', '打合せ 7', 'creator7'),
    (8, 'ms_evt_0008', '打合せ 8', 'creator8'),
    (9, 'ms_evt_0009', '打合せ 9', 'creator9'),
    (10, 'ms_evt_0010', '打合せ 10', 'creator10');
-- Insert 15 user attendees
INSERT INTO meeting_attendee_user (meeting_id, user_id)
VALUES (1, 101),
    (1, 102),
    (2, 103),
    (3, 104),
    (3, 105),
    (4, 106),
    (5, 107),
    (6, 108),
    (6, 109),
    (7, 110),
    (8, 111),
    (9, 112),
    (9, 113),
    (10, 114),
    (10, 115);
-- Insert 15 banker attendees
INSERT INTO meeting_attendee_banker (meeting_id, banker_id, banker_name)
VALUES (1, 201, 'Banker A'),
    (1, 202, 'Banker B'),
    (2, 203, 'Banker C'),
    (3, 204, 'Banker D'),
    (3, 205, 'Banker E'),
    (4, 206, 'Banker F'),
    (5, 207, 'Banker G'),
    (6, 208, 'Banker H'),
    (6, 209, 'Banker I'),
    (7, 210, 'Banker J'),
    (8, 211, 'Banker K'),
    (9, 212, 'Banker L'),
    (9, 213, 'Banker M'),
    (10, 214, 'Banker N'),
    (10, 215, 'Banker O');