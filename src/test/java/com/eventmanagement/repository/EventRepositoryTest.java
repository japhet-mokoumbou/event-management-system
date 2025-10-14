// src/test/java/com/eventmanagement/repository/EventRepositoryTest.java
package com.eventmanagement.repository;

import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests du repository EventRepository")
class EventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    private User user;
    private Event event1;
    private Event event2;
    private Event event3;

    @BeforeEach
    void setUp() {
        // Créer un utilisateur
        user = new User();
        user.setEmail("test@example.com");
        user.setFullname("John Doe");
        user.setPasswordHash("hashedPassword");
        user.setEnabled(true);
        user = entityManager.persistAndFlush(user);

        // Créer des événements
        event1 = new Event();
        event1.setTitle("Concert Rock");
        event1.setDescription("Super concert");
        event1.setCity("Paris");
        event1.setStartsAt(LocalDateTime.now().plusDays(1));
        event1.setEndsAt(LocalDateTime.now().plusDays(2));
        event1.setCreatedBy(user);
        event1 = entityManager.persistAndFlush(event1);

        event2 = new Event();
        event2.setTitle("Festival Jazz");
        event2.setDescription("Festival de jazz");
        event2.setCity("Lyon");
        event2.setStartsAt(LocalDateTime.now().plusDays(3));
        event2.setEndsAt(LocalDateTime.now().plusDays(4));
        event2.setCreatedBy(user);
        event2 = entityManager.persistAndFlush(event2);

        event3 = new Event();
        event3.setTitle("Concert Classique");
        event3.setDescription("Musique classique");
        event3.setCity("Paris");
        event3.setStartsAt(LocalDateTime.now().minusDays(1)); // Passé
        event3.setEndsAt(LocalDateTime.now().minusDays(1).plusHours(2));
        event3.setCreatedBy(user);
        event3 = entityManager.persistAndFlush(event3);
    }

    @Test
    @DisplayName("Devrait trouver des événements par ville")
    void shouldFindEventsByCity() {
        // Act
        Page<Event> result = eventRepository.findByCity("Paris", PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Event::getCity)
                .containsOnly("Paris");
    }

    @Test
    @DisplayName("Devrait trouver des événements futurs")
    void shouldFindFutureEvents() {
        // Act
        Page<Event> result = eventRepository.findByStartsAtAfter(LocalDateTime.now(), PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Event::getTitle)
                .containsExactlyInAnyOrder("Concert Rock", "Festival Jazz");
    }

    @Test
    @DisplayName("Devrait trouver des événements futurs non annulés")
    void shouldFindFutureNonCancelledEvents() {
        // Act
        Page<Event> result = eventRepository.findByStartsAtAfterAndCancelledFalse(
                LocalDateTime.now(), PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Event::getTitle)
                .containsExactlyInAnyOrder("Concert Rock", "Festival Jazz");
    }

    @Test
    @DisplayName("Devrait rechercher des événements par terme")
    void shouldSearchEventsByTerm() {
        // Act
        Page<Event> result = eventRepository.searchEvents("concert", PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Event::getTitle)
                .containsExactlyInAnyOrder("Concert Rock", "Concert Classique");
    }

    @Test
    @DisplayName("Devrait trouver des événements par créateur")
    void shouldFindEventsByCreator() {
        // Act
        Page<Event> result = eventRepository.findByCreatedById(user.getId(), PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent()).extracting(Event::getCreatedBy)
                .containsOnly(user);
    }

    @Test
    @DisplayName("Devrait trouver des événements par titre")
    void shouldFindEventsByTitle() {
        // Act
        Page<Event> result = eventRepository.findByTitleContainingIgnoreCase("concert", PageRequest.of(0, 10));

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Event::getTitle)
                .containsExactlyInAnyOrder("Concert Rock", "Concert Classique");
    }
}