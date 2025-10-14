package com.eventmanagement.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Tests de l'entité Event")
public class EventTest {

    // declaration des objets utilisés dans les tests
    private Event event;
    private User user;

    @BeforeEach
    void setUp(){
        event = new Event();
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Devrait créer un événement avec des données valides")
    void shouldCreateEventWithValidData(){
        // Arrange (preparation)
        String title = "Concert Rock";
        String description = "Un super concert de rock";
        String city = "Paris";
        LocalDateTime startsAt = LocalDateTime.now().plusDays(1);
        LocalDateTime endsAt = LocalDateTime.now().plusDays(2);

        // Act (Action)
        event.setTitle(title);
        event.setDescription(description);
        event.setCity(city);
        event.setStartsAt(startsAt);
        event.setEndsAt(endsAt);
        event.setCreatedBy(user);

        // Assert (verification)
        assertThat(event.getTitle()).isEqualTo(title);
        assertThat(event.getDescription()).isEqualTo(description);
        assertThat(event.getCity()).isEqualTo(city);
        assertThat(event.getStartsAt()).isEqualTo(startsAt);
        assertThat(event.getEndsAt()).isEqualTo(endsAt);
        assertThat(event.getCreatedBy()).isEqualTo(user);
        assertThat(event.isCancelled()).isFalse();
    }

    @Test
    @DisplayName("Devrait annuler un événement")
    void shouldCancelEvent(){

        event.setCancelled(false);

        event.setCancelled(true);

        assertThat(event.isCancelled()).isTrue();

    }

    @Test
    @DisplayName("Devrait avoir un état non annulé par défaut")
    void shouldHaveNonCancellledStateByDefault(){

        // Assert
        assertThat(event.isCancelled()).isFalse();
    }
}
