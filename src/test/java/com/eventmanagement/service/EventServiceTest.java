// src/test/java/com/eventmanagement/service/EventServiceTest.java
package com.eventmanagement.service;

import com.eventmanagement.dto.EventRequestDTO;
import com.eventmanagement.dto.EventResponseDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.TicketType;
import com.eventmanagement.entity.User;
import com.eventmanagement.mapper.EventMapper;
import com.eventmanagement.mapper.TicketTypeMapper;
import com.eventmanagement.repository.EventRepository;
import com.eventmanagement.repository.ReservationRepository;
import com.eventmanagement.repository.TicketTypeRepository;
import com.eventmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du service EventService")
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private TicketTypeMapper ticketTypeMapper;

    @Mock
    private TicketType ticketType;

    @InjectMocks
    private EventService eventService;

    private EventRequestDTO eventRequestDTO;
    private Event event;
    private User user;
    private EventResponseDTO eventResponseDTO;

    @BeforeEach
    void setUp() {
        // Arrange
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
        event.setCreatedBy(user);

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(1L);
        eventResponseDTO.setTitle("Test Event");
    }

    @Test
    @DisplayName("Devrait créer un événement avec succès")
    void shouldCreateEventSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventMapper.toEntity(eventRequestDTO)).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        // Act
        EventResponseDTO result = eventService.createEvent(eventRequestDTO, 1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Event");

        verify(userRepository).findById(1L);
        verify(eventMapper).toEntity(eventRequestDTO);
        verify(eventRepository).save(event);
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    @DisplayName("Devrait lever une exception quand l'utilisateur n'existe pas")
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> eventService.createEvent(eventRequestDTO, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Utilisateur introuvable");
    }

    @Test
    @DisplayName("Devrait lever une exception quand la date de fin est avant la date de début")
    void shouldThrowExceptionWhenEndDateBeforeStartDate() {
        // Arrange
        eventRequestDTO.setStartsAt(LocalDateTime.now().plusDays(2));
        eventRequestDTO.setEndsAt(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> eventService.createEvent(eventRequestDTO, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("La date de fin doit être après la date de début");
    }

    @Test
    @DisplayName("Devrait lever une exception quand la date de début est dans le passé")
    void shouldThrowExceptionWhenStartDateInPast() {
        // Arrange
        eventRequestDTO.setStartsAt(LocalDateTime.now().minusDays(1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> eventService.createEvent(eventRequestDTO, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("La date de début doit être dans le futur");
    }

    @Test
    @DisplayName("Devrait trouver un événement par ID")
    void shouldFindEventById() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        // Act
        EventResponseDTO result = eventService.findById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(eventRepository).findById(1L);
        verify(eventMapper).toResponseDTO(event);
    }

    @Test
    @DisplayName("Devrait lever une exception quand l'événement n'existe pas")
    void shouldThrowExceptionWhenEventNotFound() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> eventService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Evenement introuvale");
    }

    @Test
    @DisplayName("Devrait retourner tous les événements")
    void shouldReturnAllEvents() {
        // Arrange
        Page<Event> eventPage = new PageImpl<>(List.of(event));
        Page<EventResponseDTO> expectedPage = new PageImpl<>(List.of(eventResponseDTO));

        when(eventRepository.findAll(any(Pageable.class))).thenReturn(eventPage);
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        // Act
        Page<EventResponseDTO> result = eventService.findAll(mock(Pageable.class));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Devrait annuler un événement")
    void shouldCancelEvent() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);

        // Act
        eventService.cancelEvent(1L, 1L);

        // Assert
        assertThat(event.isCancelled()).isTrue();
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(event);
    }

    @Test
    @DisplayName("Devrait lever une exception quand l'utilisateur n'est pas autorisé à annuler l'événement")
    void shouldThrowExceptionWhenUserNotAuthorizedToCancel() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        event.setCreatedBy(otherUser);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Act & Assert
        assertThatThrownBy(() -> eventService.cancelEvent(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Non autorisé");
    }
}