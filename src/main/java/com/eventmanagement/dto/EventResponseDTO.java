package com.eventmanagement.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class EventResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String city;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private UserResponseDTO createdBy;
    private List<TicketTypeResponseDTO> ticketTypes;
    private Instant createdAt;
    private Instant updatedAt;

    public EventResponseDTO() {
    }

    public EventResponseDTO(Long id, String title, String description, String city, LocalDateTime startsAt, LocalDateTime endsAt, UserResponseDTO createdBy, List<TicketTypeResponseDTO> ticketTypes, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.city = city;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.createdBy = createdBy;
        this.ticketTypes = ticketTypes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }

    public UserResponseDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserResponseDTO createdBy) {
        this.createdBy = createdBy;
    }

    public List<TicketTypeResponseDTO> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeResponseDTO> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
