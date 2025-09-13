package com.example.tkrlabo.meeting.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DaoConverterTest {

    @Nested
    @DisplayName("convertList")
    class ConvertList {

        @DisplayName("有効な入力の場合、正しくMeetingリストに変換される")
        @Test
        void whenValidInput_shouldConvertToMeetingList() {
            // Arrange
            var now = LocalDateTime.now();
            var meetingDao1 = MeetingDao.of(1L, "event-1", "Meeting 1", "creator1", now, now);
            var meetingDao2 = MeetingDao.of(2L, "event-2", "Meeting 2", "creator2", now, now);
            var meetings = List.of(meetingDao1, meetingDao2);

            var bankerDao1 = MeetingAttendeeBankerDao.of(1L, 1001L, "Banker A", 1L);
            var bankerDao2 = MeetingAttendeeBankerDao.of(2L, 1002L, "Banker B", 2L);
            var attendeeBankers = List.of(bankerDao1, bankerDao2);

            var userDao1 = MeetingAttendeeUserDao.of(1L, 100L, 1L);
            var userDao2 = MeetingAttendeeUserDao.of(2L, 200L, 1L);
            var userDao3 = MeetingAttendeeUserDao.of(3L, 300L, 2L);
            var attendeeUsers = List.of(userDao1, userDao2, userDao3);

            // Act
            var result = DaoConverter.convertList(meetings, attendeeBankers, attendeeUsers);

            // Assert
            assertThat(result).hasSize(2);

            var meeting1 = result.stream()
                    .filter(m -> m.id().equals(1L))
                    .findFirst()
                    .orElseThrow();
            assertThat(meeting1.id()).isEqualTo(1L);
            assertThat(meeting1.msEventId()).isEqualTo("event-1");
            assertThat(meeting1.subject()).isEqualTo("Meeting 1");
            assertThat(meeting1.creator()).isEqualTo("creator1");
            assertThat(meeting1.createdAt()).isEqualTo(now);
            assertThat(meeting1.updatedAt()).isEqualTo(now);
            assertThat(meeting1.attendeeUsers()).hasSize(2);
            assertThat(meeting1.attendeeBankers()).hasSize(1);

            var user1 = meeting1.attendeeUsers().get(0);
            assertThat(user1.id()).isEqualTo(1L);
            assertThat(user1.userId()).isEqualTo(100L);

            var user2 = meeting1.attendeeUsers().get(1);
            assertThat(user2.id()).isEqualTo(2L);
            assertThat(user2.userId()).isEqualTo(200L);

            var banker1 = meeting1.attendeeBankers().get(0);
            assertThat(banker1.id()).isEqualTo(1L);
            assertThat(banker1.bankerId()).isEqualTo(1001L);
            assertThat(banker1.bankerName()).isEqualTo("Banker A");

            var meeting2 = result.stream()
                    .filter(m -> m.id().equals(2L))
                    .findFirst()
                    .orElseThrow();
            assertThat(meeting2.id()).isEqualTo(2L);
            assertThat(meeting2.msEventId()).isEqualTo("event-2");
            assertThat(meeting2.subject()).isEqualTo("Meeting 2");
            assertThat(meeting2.creator()).isEqualTo("creator2");
            assertThat(meeting2.attendeeUsers()).hasSize(1);
            assertThat(meeting2.attendeeBankers()).hasSize(1);

            var user3 = meeting2.attendeeUsers().get(0);
            assertThat(user3.id()).isEqualTo(3L);
            assertThat(user3.userId()).isEqualTo(300L);

            var banker2 = meeting2.attendeeBankers().get(0);
            assertThat(banker2.id()).isEqualTo(2L);
            assertThat(banker2.bankerId()).isEqualTo(1002L);
            assertThat(banker2.bankerName()).isEqualTo("Banker B");
        }

        @DisplayName("参加者が存在しない場合、空の参加者リストでMeetingが作成される")
        @Test
        void whenNoAttendees_shouldReturnMeetingsWithEmptyLists() {
            // Arrange
            var now = LocalDateTime.now();
            var meetingDao = MeetingDao.of(1L, "event-1", "Meeting 1", "creator1", now, now);
            var meetings = List.of(meetingDao);
            var attendeeBankers = List.<MeetingAttendeeBankerDao>of();
            var attendeeUsers = List.<MeetingAttendeeUserDao>of();

            // Act
            var result = DaoConverter.convertList(meetings, attendeeBankers, attendeeUsers);

            // Assert
            assertThat(result).hasSize(1);
            var meeting = result.get(0);
            assertThat(meeting.id()).isEqualTo(1L);
            assertThat(meeting.attendeeUsers()).isEmpty();
            assertThat(meeting.attendeeBankers()).isEmpty();
        }

        @DisplayName("空のミーティングリストの場合、空のリストが返される")
        @Test
        void whenEmptyMeetingsList_shouldReturnEmptyList() {
            // Arrange
            var meetings = List.<MeetingDao>of();
            var attendeeBankers = List.<MeetingAttendeeBankerDao>of();
            var attendeeUsers = List.<MeetingAttendeeUserDao>of();

            // Act
            var result = DaoConverter.convertList(meetings, attendeeBankers, attendeeUsers);

            // Assert
            assertThat(result).isEmpty();
        }

        @DisplayName("同一ミーティングに複数の参加者がいる場合、正しくグループ化される")
        @Test
        void whenMultipleAttendeesForSameMeeting_shouldGroupCorrectly() {
            // Arrange
            var now = LocalDateTime.now();
            var meetingDao = MeetingDao.of(1L, "event-1", "Meeting 1", "creator1", now, now);
            var meetings = List.of(meetingDao);

            var banker1 = MeetingAttendeeBankerDao.of(1L, 1001L, "Banker A", 1L);
            var banker2 = MeetingAttendeeBankerDao.of(2L, 1002L, "Banker B", 1L);
            var attendeeBankers = List.of(banker1, banker2);

            var user1 = MeetingAttendeeUserDao.of(1L, 100L, 1L);
            var user2 = MeetingAttendeeUserDao.of(2L, 200L, 1L);
            var user3 = MeetingAttendeeUserDao.of(3L, 300L, 1L);
            var attendeeUsers = List.of(user1, user2, user3);

            // Act
            var result = DaoConverter.convertList(meetings, attendeeBankers, attendeeUsers);

            // Assert
            assertThat(result).hasSize(1);
            var meeting = result.get(0);
            assertThat(meeting.attendeeUsers()).hasSize(3);
            assertThat(meeting.attendeeBankers()).hasSize(2);
        }
    }
}
