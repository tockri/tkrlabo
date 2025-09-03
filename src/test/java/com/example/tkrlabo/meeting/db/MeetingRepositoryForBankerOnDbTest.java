package com.example.tkrlabo.meeting.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Rollback
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MeetingRepositoryForBankerOnDb.class)
@Sql("MeetingRepositoryForBankerOnDbTest-data.sql")
class MeetingRepositoryForBankerOnDbTest {

    @Autowired
    private MeetingRepositoryForBankerOnDb meetingRepositoryOnDb;

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
            assertThat(meeting1.id()).isEqualTo(1L);
            assertThat(meeting1.subject()).isEqualTo("Meeting 1");
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
            assertThat(meeting2.id()).isEqualTo(2L);
            assertThat(meeting2.subject()).isEqualTo("Meeting 2");
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
            assertThat(meeting.subject()).isEqualTo("Meeting 2");

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

        // テストケースを後で追加
    }
}