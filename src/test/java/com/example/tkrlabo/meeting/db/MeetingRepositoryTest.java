package com.example.tkrlabo.meeting.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingRepositoryTest {

    @Container
    @SuppressWarnings("resource")
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("tkrlabo")
            .withUsername("test")
            .withPassword("test")
            .withFileSystemBind("tools/mysql/init.d", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    private MeetingRepository meetingRepository;

    private static boolean setupDone = false;

    @BeforeEach
    void setupTestData() {
        if (setupDone) {
            return;
        }
        setupDone = true;
        // Meeting 1 with banker 1001 and users 100, 200
        MeetingDao meeting1 = MeetingDao.of(null, "event-1", "Meeting 1", "organizer1", 
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        MeetingAttendeeUserDao user1 = MeetingAttendeeUserDao.of(null, 100L, meeting1);
        MeetingAttendeeUserDao user2 = MeetingAttendeeUserDao.of(null, 200L, meeting1);
        MeetingAttendeeBankerDao banker1 = MeetingAttendeeBankerDao.of(null, 1001L, "Banker A", meeting1);
        meeting1.setAttendeeUsers(List.of(user1, user2));
        meeting1.setAttendeeBankers(List.of(banker1));
        meetingRepository.save(meeting1);

        // Meeting 2 with banker 1002 and user 300
        MeetingDao meeting2 = MeetingDao.of(null, "event-2", "Meeting 2", "organizer2", 
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        MeetingAttendeeUserDao user3 = MeetingAttendeeUserDao.of(null, 300L, meeting2);
        MeetingAttendeeBankerDao banker2 = MeetingAttendeeBankerDao.of(null, 1002L, "Banker B", meeting2);
        meeting2.setAttendeeUsers(List.of(user3));
        meeting2.setAttendeeBankers(List.of(banker2));
        meetingRepository.save(meeting2);

        // Meeting 3 with both bankers 1001, 1003 and user 400
        MeetingDao meeting3 = MeetingDao.of(null, "event-3", "Meeting 3", "organizer3", 
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        MeetingAttendeeUserDao user4 = MeetingAttendeeUserDao.of(null, 400L, meeting3);
        MeetingAttendeeBankerDao banker3 = MeetingAttendeeBankerDao.of(null, 1001L, "Banker A", meeting3);
        MeetingAttendeeBankerDao banker4 = MeetingAttendeeBankerDao.of(null, 1003L, "Banker C", meeting3);
        meeting3.setAttendeeUsers(List.of(user4));
        meeting3.setAttendeeBankers(List.of(banker3, banker4));
        meetingRepository.save(meeting3);
    }

    @Test
    void testFindById() {
        Iterable<MeetingDao> meetings = meetingRepository.findAll();
        MeetingDao firstMeeting = meetings.iterator().next();
        Long meetingId = firstMeeting.getId();

        Optional<MeetingDao> found = meetingRepository.findById(meetingId);
        assertThat(found).isPresent();
        assertThat(found.get().getAttendeeUsers()).isNotNull();
        assertThat(found.get().getAttendeeBankers()).isNotNull();
        assertThat(found.get().getAttendeeUsers().size()).isGreaterThan(0);
        assertThat(found.get().getAttendeeBankers().size()).isGreaterThan(0);
    }

    @Test
    void testFindByBankerId() {
        List<MeetingDao> meetings = meetingRepository.findByBankerId(1001L);
        assertThat(meetings).hasSize(2); // Meeting 1 and Meeting 3
        
        for (MeetingDao meeting : meetings) {
            assertThat(meeting.getAttendeeUsers()).isNotNull();
            assertThat(meeting.getAttendeeBankers()).isNotNull();
            assertThat(meeting.getAttendeeBankers().stream()
                    .anyMatch(banker -> banker.getBankerId().equals(1001L))).isTrue();
        }
    }
}