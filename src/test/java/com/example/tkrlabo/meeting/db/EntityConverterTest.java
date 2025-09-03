package com.example.tkrlabo.meeting.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.tkrlabo.meeting.domain.entity.MeetingAttendeeBankerInput;
import com.example.tkrlabo.meeting.domain.entity.MeetingAttendeeUserInput;
import com.example.tkrlabo.meeting.domain.entity.MeetingInput;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EntityConverterTest {

    @Nested
    @DisplayName("convertInput")
    class ConvertInput {

        @DisplayName("有効な入力の場合、DaoSetに正しく変換される")
        @Test
        void whenValidInput_shouldConvertToDaoSet() {
            // Arrange
            var userInput1 = new MeetingAttendeeUserInput(100L);
            var userInput2 = new MeetingAttendeeUserInput(200L);
            var bankerInput1 = new MeetingAttendeeBankerInput(1001L, "Banker A");
            var bankerInput2 = new MeetingAttendeeBankerInput(1002L, "Banker B");

            var meetingInput = new MeetingInput(
                    "event-1",
                    "Meeting 1",
                    "creator1",
                    List.of(userInput1, userInput2),
                    List.of(bankerInput1, bankerInput2));

            var beforeTime = LocalDateTime.now().minusSeconds(1);

            // Act
            var result = EntityConverter.convertInput(meetingInput);

            // Assert
            var afterTime = LocalDateTime.now().plusSeconds(1);

            // MeetingDao検証
            var meetingDao = result.meeting();
            assertThat(meetingDao.getId()).isNull();
            assertThat(meetingDao.getMsEventId()).isEqualTo("event-1");
            assertThat(meetingDao.getSubject()).isEqualTo("Meeting 1");
            assertThat(meetingDao.getCreator()).isEqualTo("creator1");
            assertThat(meetingDao.getCreatedAt()).isBetween(beforeTime, afterTime);
            assertThat(meetingDao.getUpdatedAt()).isBetween(beforeTime, afterTime);

            // MeetingAttendeeUserDao検証
            var attendeeUsers = result.attendeeUsers();
            assertThat(attendeeUsers).hasSize(2);

            var userDao1 = attendeeUsers.get(0);
            assertThat(userDao1.getId()).isNull();
            assertThat(userDao1.getUserId()).isEqualTo(100L);
            assertThat(userDao1.getMeetingId()).isNull();

            var userDao2 = attendeeUsers.get(1);
            assertThat(userDao2.getId()).isNull();
            assertThat(userDao2.getUserId()).isEqualTo(200L);
            assertThat(userDao2.getMeetingId()).isNull();

            // MeetingAttendeeBankerDao検証
            var attendeeBankers = result.attendeeBankers();
            assertThat(attendeeBankers).hasSize(2);

            var bankerDao1 = attendeeBankers.get(0);
            assertThat(bankerDao1.getId()).isNull();
            assertThat(bankerDao1.getBankerId()).isEqualTo(1001L);
            assertThat(bankerDao1.getBankerName()).isEqualTo("Banker A");
            assertThat(bankerDao1.getMeetingId()).isNull();

            var bankerDao2 = attendeeBankers.get(1);
            assertThat(bankerDao2.getId()).isNull();
            assertThat(bankerDao2.getBankerId()).isEqualTo(1002L);
            assertThat(bankerDao2.getBankerName()).isEqualTo("Banker B");
            assertThat(bankerDao2.getMeetingId()).isNull();
        }

        @DisplayName("参加者が存在しない場合、空の参加者リストでDaoSetが作成される")
        @Test
        void whenNoAttendees_shouldReturnDaoSetWithEmptyLists() {
            // Arrange
            var meetingInput = new MeetingInput(
                    "event-1",
                    "Meeting 1",
                    "creator1",
                    List.of(),
                    List.of());

            var beforeTime = LocalDateTime.now().minusSeconds(1);

            // Act
            var result = EntityConverter.convertInput(meetingInput);

            // Assert
            var afterTime = LocalDateTime.now().plusSeconds(1);

            var meetingDao = result.meeting();
            assertThat(meetingDao.getId()).isNull();
            assertThat(meetingDao.getMsEventId()).isEqualTo("event-1");
            assertThat(meetingDao.getSubject()).isEqualTo("Meeting 1");
            assertThat(meetingDao.getCreator()).isEqualTo("creator1");
            assertThat(meetingDao.getCreatedAt()).isBetween(beforeTime, afterTime);
            assertThat(meetingDao.getUpdatedAt()).isBetween(beforeTime, afterTime);

            assertThat(result.attendeeUsers()).isEmpty();
            assertThat(result.attendeeBankers()).isEmpty();
        }

        @DisplayName("ユーザーのみ参加者の場合、銀行員リストが空でDaoSetが作成される")
        @Test
        void whenOnlyUserAttendees_shouldReturnDaoSetWithEmptyBankersList() {
            // Arrange
            var userInput1 = new MeetingAttendeeUserInput(100L);
            var userInput2 = new MeetingAttendeeUserInput(200L);

            var meetingInput = new MeetingInput(
                    "event-1",
                    "Meeting 1",
                    "creator1",
                    List.of(userInput1, userInput2),
                    List.of());

            // Act
            var result = EntityConverter.convertInput(meetingInput);

            // Assert
            var meetingDao = result.meeting();
            assertThat(meetingDao.getMsEventId()).isEqualTo("event-1");

            assertThat(result.attendeeUsers()).hasSize(2);
            assertThat(result.attendeeBankers()).isEmpty();

            var userDao1 = result.attendeeUsers().get(0);
            assertThat(userDao1.getUserId()).isEqualTo(100L);

            var userDao2 = result.attendeeUsers().get(1);
            assertThat(userDao2.getUserId()).isEqualTo(200L);
        }

        @DisplayName("銀行員のみ参加者の場合、ユーザーリストが空でDaoSetが作成される")
        @Test
        void whenOnlyBankerAttendees_shouldReturnDaoSetWithEmptyUsersList() {
            // Arrange
            var bankerInput1 = new MeetingAttendeeBankerInput(1001L, "Banker A");
            var bankerInput2 = new MeetingAttendeeBankerInput(1002L, "Banker B");

            var meetingInput = new MeetingInput(
                    "event-1",
                    "Meeting 1",
                    "creator1",
                    List.of(),
                    List.of(bankerInput1, bankerInput2));

            // Act
            var result = EntityConverter.convertInput(meetingInput);

            // Assert
            var meetingDao = result.meeting();
            assertThat(meetingDao.getMsEventId()).isEqualTo("event-1");

            assertThat(result.attendeeUsers()).isEmpty();
            assertThat(result.attendeeBankers()).hasSize(2);

            var bankerDao1 = result.attendeeBankers().get(0);
            assertThat(bankerDao1.getBankerId()).isEqualTo(1001L);
            assertThat(bankerDao1.getBankerName()).isEqualTo("Banker A");

            var bankerDao2 = result.attendeeBankers().get(1);
            assertThat(bankerDao2.getBankerId()).isEqualTo(1002L);
            assertThat(bankerDao2.getBankerName()).isEqualTo("Banker B");
        }
    }
}