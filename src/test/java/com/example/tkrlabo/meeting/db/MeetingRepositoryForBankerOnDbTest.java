package com.example.tkrlabo.meeting.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.tkrlabo.meeting.db.callback.MeetingDaoCallback;
import com.example.tkrlabo.meeting.domain.dto.MeetingInput;
import com.example.tkrlabo.meeting.domain.entity.Meeting;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@Transactional
@Rollback
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        MeetingRepositoryForBankerOnDb.class,
        ValidationAutoConfiguration.class,
        MeetingDaoCallback.class
})
@Sql("MeetingRepositoryForBankerOnDbTest-data.sql")
@Testcontainers
class MeetingRepositoryForBankerOnDbTest {

    @Autowired
    private MeetingRepositoryForBankerOnDb meetingRepositoryOnDb;

    @Autowired
    private MeetingDaoRepository meetingDaoRepository;

    @Autowired
    private MeetingAttendeeUserDaoRepository userDaoRepository;

    @Autowired
    private MeetingAttendeeBankerDaoRepository bankerDaoRepository;

    @Nested
    @DisplayName("findByBankerId")
    class FindByBankerId {

        @DisplayName("指定した銀行員IDが参加するミーティングの場合、該当するミーティングが返される")
        @Test
        void whenBankerExists_shouldReturnMeetingsWithBanker() {
            // Arrange
            var bankerId = 1001L;
            var fromDate = LocalDate.of(2020, 1, 1);

            // Act
            var result = meetingRepositoryOnDb.findByBankerId(bankerId, fromDate);

            // Assert
            assertThat(result).hasSize(2); // Meeting1とMeeting2の両方にbanker 1001が参加

            // Meeting1の検証
            var meeting1 = result.stream()
                    .filter(m -> m.msEventId().equals("event-1"))
                    .findFirst()
                    .orElseThrow();
            assertThat(meeting1.id()).isEqualTo(10001L);
            assertThat(meeting1.subject()).isEqualTo("meeting 1");
            assertThat(meeting1.creator()).isEqualTo("creator1");
            assertThat(meeting1.attendeeUsers()).hasSize(1);
            assertThat(meeting1.attendeeBankers()).hasSize(1);

            var m1user1 = meeting1.attendeeUsers().get(0);
            assertThat(m1user1.userId()).isEqualTo(100L);

            var m1banker1 = meeting1.attendeeBankers().get(0);
            assertThat(m1banker1.bankerId()).isEqualTo(1001L);
            assertThat(m1banker1.bankerName()).isEqualTo("Banker A");

            // Meeting2の検証
            var meeting2 = result.stream()
                    .filter(m -> m.msEventId().equals("event-2"))
                    .findFirst()
                    .orElseThrow();
            assertThat(meeting2.id()).isEqualTo(10002L);
            assertThat(meeting2.subject()).isEqualTo("meeting 2");
            assertThat(meeting2.creator()).isEqualTo("creator2");
            assertThat(meeting2.attendeeUsers()).hasSize(2);
            assertThat(meeting2.attendeeBankers()).hasSize(2);

            var m2user1 = meeting2.attendeeUsers().get(0);
            assertThat(m2user1.userId()).isEqualTo(101L);
            var m2user2 = meeting2.attendeeUsers().get(1);
            assertThat(m2user2.userId()).isEqualTo(102L);

            var m2banker1 = meeting2.attendeeBankers().get(0);
            assertThat(m2banker1.bankerId()).isEqualTo(1001L);
            var m2banker2 = meeting2.attendeeBankers().get(1);
            assertThat(m2banker2.bankerId()).isEqualTo(1002L);
        }

        @DisplayName("指定した銀行員IDが参加しないミーティングの場合、空のリストが返される")
        @Test
        void whenBankerDoesNotExist_shouldReturnEmptyList() {
            // Arrange
            var bankerId = 9999L; // 存在しない銀行員ID
            var fromDate = LocalDate.of(2020, 1, 1);

            // Act
            var result = meetingRepositoryOnDb.findByBankerId(bankerId, fromDate);

            // Assert
            assertThat(result).isEmpty();
        }

        @DisplayName("特定の銀行員のみが参加するミーティングの場合、該当するミーティングが返される")
        @Test
        void whenSpecificBankerOnly_shouldReturnCorrectMeeting() {
            // Arrange
            var bankerId = 1002L; // Meeting2のみに参加
            var fromDate = LocalDate.of(2020, 1, 1);

            // Act
            var result = meetingRepositoryOnDb.findByBankerId(bankerId, fromDate);

            // Assert
            assertThat(result).hasSize(1);

            var meeting = result.get(0);
            assertThat(meeting.msEventId()).isEqualTo("event-2");
            assertThat(meeting.subject()).isEqualTo("meeting 2");

            // banker 1002が含まれていることを確認
            var bankerIds = meeting.attendeeBankers().stream()
                    .map(b -> b.bankerId())
                    .toList();
            assertThat(bankerIds).contains(1002L);
        }
    }

    @Nested
    @DisplayName("insert")
    class Insert {
        private MeetingDao findMeetingByEventId(String msEventId) {
            return StreamSupport.stream(meetingDaoRepository.findAll().spliterator(), false)
                    .filter(m -> Objects.equals(m.getMsEventId(), msEventId))
                    .findFirst()
                    .orElse(null);
        }

        @DisplayName("ユーザー・銀行員を含む入力を保存し、関連も保存される")
        @Test
        void whenValidInput_shouldInsertMeetingAndAttendees() {
            // Arrange
            var input = new MeetingInput(
                    "event-insert-1",
                    "Meeting Insert 1",
                    "creator-i1",
                    List.of(
                            new MeetingInput.AttendeeUser(201L),
                            new MeetingInput.AttendeeUser(202L)),
                    List.of(
                            new MeetingInput.AttendeeBanker(1101L, "Banker X")));

            // Act
            meetingRepositoryOnDb.insert(input);

            // Assert (raw repositories)
            var meeting = findMeetingByEventId("event-insert-1");
            assertThat(meeting).isNotNull();
            assertThat(meeting.getMsEventId()).isEqualTo("event-insert-1");
            assertThat(meeting.getSubject()).isEqualTo("meeting insert 1");
            assertThat(meeting.getCreator()).isEqualTo("creator-i1");
            assertThat(meeting.getId()).isNotNull();
            assertThat(meeting.getCreatedAt()).isNotNull();
            assertThat(meeting.getUpdatedAt()).isNotNull();

            var users = userDaoRepository.findByMeetingIds(List.of(meeting.getId()));
            assertThat(users).hasSize(2);
            assertThat(users).extracting("userId").containsExactlyInAnyOrder(201L, 202L);

            var bankers = bankerDaoRepository.findByMeetingIds(List.of(meeting.getId()));
            assertThat(bankers).hasSize(1);
            assertThat(bankers.get(0).getBankerName()).isEqualTo("Banker X");
        }

        private record MeetingInputData(String msEventId, String subject, String creator,
                Collection<MeetingInput.AttendeeUser> users,
                Collection<MeetingInput.AttendeeBanker> bankers) {
        }

        static Collection<MeetingInputData> invalidInputs() {
            return List.of(
                    // attenddeeUsersが空
                    new MeetingInputData(
                            "event-invalid-u-empty",
                            "Meeting Invalid",
                            "creator",
                            List.of(),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))),
                    // attenddeeBankersが空
                    new MeetingInputData(
                            "event-invalid-b-empty",
                            "Meeting Invalid",
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of()),
                    // attendeeUsersのuserIdがnull
                    new MeetingInputData(
                            "event-invalid-userid",
                            "Meeting Invalid",
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(null)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))),
                    // attendeeBankersのbankerIdがnull
                    new MeetingInputData(
                            "event-invalid-bname",
                            "Meeting Invalid",
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(null, "B"))),
                    // attendeeBankersのbankerNameが空
                    new MeetingInputData(
                            "event-invalid-bname2",
                            "Meeting Invalid",
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, ""))),
                    // msEventIdがnull
                    new MeetingInputData(
                            null,
                            "Meeting Invalid",
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))),
                    // msEventIdが空
                    new MeetingInputData(
                            "",
                            "Meeting Invalid",
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))),
                    // subjectがnull
                    new MeetingInputData(
                            "event-invalid-subject",
                            null,
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))),
                    // subjectが空
                    new MeetingInputData(
                            "event-invalid-subject",
                            "",
                            "creator",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))),
                    // creatorがnull
                    new MeetingInputData(
                            "event-invalid-creator",
                            "Meeting Invalid",
                            null,
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))),
                    // creatorが空
                    new MeetingInputData(
                            "event-invalid-creator",
                            "Meeting Invalid",
                            "",
                            List.of(new MeetingInput.AttendeeUser(1L)),
                            List.of(new MeetingInput.AttendeeBanker(2001L, "B"))));
        }

        @MethodSource("invalidInputs")
        @ParameterizedTest
        @DisplayName("不正な入力の場合、制約違反が発生する")
        void whenInvalidInput_shouldFailValidation(MeetingInputData data) {
            // Arrange
            var input = new MeetingInput(
                    data.msEventId,
                    data.subject,
                    data.creator,
                    List.copyOf(data.users),
                    List.copyOf(data.bankers));

            // Act & Assert
            assertThatThrownBy(() -> meetingRepositoryOnDb.insert(input))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

}
