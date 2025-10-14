// src/test/java/com/eventmanagement/controller/EventControllerTest.java
package com.eventmanagement.controller;

import com.eventmanagement.dto.EventRequestDTO;
import com.eventmanagement.dto.EventResponseDTO;
import com.eventmanagement.entity.User;
import com.eventmanagement.repository.UserRepository;
import com.eventmanagement.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
@DisplayName("Tests du contrôleur EventController")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private EventRequestDTO eventRequestDTO;
    private EventResponseDTO eventResponseDTO;

    @BeforeEach
    void setUp() {
        eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setTitle("Test Event");
        eventRequestDTO.setDescription("Test Description");
        eventRequestDTO.setCity("Test City");
        eventRequestDTO.setStartsAt(LocalDateTime.now().plusDays(1));
        eventRequestDTO.setEndsAt(LocalDateTime.now().plusDays(2));

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(1L);
        eventResponseDTO.setTitle("Test Event");
        eventResponseDTO.setDescription("Test Description");
        eventResponseDTO.setCity("Test City");
    }

    @Test
    @DisplayName("Devrait créer un événement avec succès")
    @WithMockUser(roles = "ORGANIZER")
    void shouldCreateEventSuccessfully() throws Exception {
        // Arrange
        when(eventService.createEvent(any(EventRequestDTO.class), anyLong()))
                .thenReturn(eventResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/event")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequestDTO))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.city").value("Test City"));
    }

    @Test
    @DisplayName("Devrait retourner une erreur 400 pour une requête invalide")
    @WithMockUser(roles = "ORGANIZER")
    void shouldReturnBadRequestForInvalidRequest() throws Exception {
        // Arrange
        when(eventService.createEvent(any(EventRequestDTO.class), anyLong()))
                .thenThrow(new IllegalArgumentException("Données invalides"));

        // Act & Assert
        mockMvc.perform(post("/api/event")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequestDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Devrait retourner un événement par ID")
    void shouldGetEventById() throws Exception {
        // Arrange
        when(eventService.findById(1L)).thenReturn(eventResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    @DisplayName("Devrait retourner 404 quand l'événement n'existe pas")
    void shouldReturnNotFoundWhenEventNotExists() throws Exception {
        // Arrange
        when(eventService.findById(1L))
                .thenThrow(new RuntimeException("Evenement introuvale"));

        // Act & Assert
        mockMvc.perform(get("/api/event/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Devrait retourner tous les événements")
    void shouldGetAllEvents() throws Exception {
        // Arrange
        Page<EventResponseDTO> page = new PageImpl<>(List.of(eventResponseDTO));
        when(eventService.findAll(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/event"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Event"));
    }

    @Test
    @DisplayName("Devrait retourner les événements à venir")
    void shouldGetUpcomingEvents() throws Exception {
        // Arrange
        Page<EventResponseDTO> page = new PageImpl<>(List.of(eventResponseDTO));
        when(eventService.findUpcoming(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/event/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Devrait rechercher des événements par terme")
    void shouldSearchEventsByTerm() throws Exception {
        // Arrange
        Page<EventResponseDTO> page = new PageImpl<>(List.of(eventResponseDTO));
        when(eventService.searchEvents("test", any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/event/search")
                        .param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Devrait retourner les événements par ville")
    void shouldGetEventsByCity() throws Exception {
        // Arrange
        Page<EventResponseDTO> page = new PageImpl<>(List.of(eventResponseDTO));
        when(eventService.findByCity("Paris", any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/event/city/Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("Devrait mettre à jour un événement")
    @WithMockUser(username = "test@example.com")
    void shouldUpdateEvent() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(eventService.updateEvent(1L, eventRequestDTO, 1L)).thenReturn(eventResponseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/event/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequestDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    @DisplayName("Devrait supprimer un événement")
    @WithMockUser(username = "test@example.com")
    void shouldDeleteEvent() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

        // Act & Assert
        mockMvc.perform(delete("/api/event/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Devrait annuler un événement")
    @WithMockUser(username = "test@example.com")
    void shouldCancelEvent() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

        // Act & Assert
        mockMvc.perform(patch("/api/event/1/cancel")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}