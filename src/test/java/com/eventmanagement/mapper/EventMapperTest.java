// src/test/java/com/eventmanagement/mapper/EventMapperTest.java
package com.eventmanagement.mapper;

import com.eventmanagement.dto.EventRequestDTO;
import com.eventmanagement.dto.EventResponseDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Tests du mapper EventMapper")
class EventMapperTest {

    private EventMapper eventMapper;
    private EventRequestDTO eventRequestDTO;
    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        eventMapper = Mappers.getMapper(EventMapper.class);

        eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setTitle("Test Event");
        eventRequestDTO.setDescription("Test Description");
        eventRequestDTO.setCity("Test City");
        eventRequestDTO.setStartsAt(LocalDateTime.now().plusDays(1));
        eventRequestDTO.setEndsAt(LocalDateTime.now().plusDays(2));

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        event = new Event();
        event.setId(1L);
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setCity("Test City");
        event.setStartsAt(LocalDateTime.now().plusDays(1));
        event.setEndsAt(LocalDateTime.now().plusDays(2));
        event.setCreatedBy(user);
    }

    @Test
    @DisplayName("Devrait mapper EventRequestDTO vers Event")
    void shouldMapEventRequestDTOToEvent() {
        // Act
        Event result = eventMapper.toEntity(eventRequestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(eventRequestDTO.getTitle());
        assertThat(result.getDescription()).isEqualTo(eventRequestDTO.getDescription());
        assertThat(result.getCity()).isEqualTo(eventRequestDTO.getCity());
        assertThat(result.getStartsAt()).isEqualTo(eventRequestDTO.getStartsAt());
        assertThat(result.getEndsAt()).isEqualTo(eventRequestDTO.getEndsAt());
        assertThat(result.getId()).isNull(); // ID ignoré
        assertThat(result.getCreatedBy()).isNull(); // CreatedBy ignoré
    }

    @Test
    @DisplayName("Devrait mapper Event vers EventResponseDTO")
    void shouldMapEventToEventResponseDTO() {
        // Act
        EventResponseDTO result = eventMapper.toResponseDTO(event);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(event.getId());
        assertThat(result.getTitle()).isEqualTo(event.getTitle());
        assertThat(result.getDescription()).isEqualTo(event.getDescription());
        assertThat(result.getCity()).isEqualTo(event.getCity());
        assertThat(result.getStartsAt()).isEqualTo(event.getStartsAt());
        assertThat(result.getEndsAt()).isEqualTo(event.getEndsAt());
        assertThat(result.getCreatedBy()).isEqualTo(event.getCreatedBy().getEmail());
    }

    @Test
    @DisplayName("Devrait mapper une liste d'événements")
    void shouldMapEventList() {
        // Arrange
        java.util.List<Event> events = java.util.List.of(event);

        // Act
        java.util.List<EventResponseDTO> result = eventMapper.toResponseDTOList(events);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(event.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(event.getTitle());
    }

    @Test
    @DisplayName("Devrait mettre à jour un événement existant")
    void shouldUpdateExistingEvent() {
        // Arrange
        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setTitle("Old Title");
        existingEvent.setDescription("Old Description");

        // Act
        eventMapper.updateEntity(eventRequestDTO, existingEvent);

        // Assert
        assertThat(existingEvent.getTitle()).isEqualTo(eventRequestDTO.getTitle());
        assertThat(existingEvent.getDescription()).isEqualTo(eventRequestDTO.getDescription());
        assertThat(existingEvent.getCity()).isEqualTo(eventRequestDTO.getCity());
        assertThat(existingEvent.getId()).isEqualTo(1L); // ID préservé
    }
}