package com.eventmanagement.dto;

import com.eventmanagement.entity.ReservationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class ReservationResponseDTO {

    private Long id;
    private UserResponseDTO user;
    private EventResponseDTO event;
    private ReservationStatus status;
    private BigDecimal totalAmount;
    private List<ReservationItemResponseDTO> items;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructeurs
    public ReservationResponseDTO() {}

    public ReservationResponseDTO(Long id, UserResponseDTO user, EventResponseDTO event,
                                  ReservationStatus status, BigDecimal totalAmount,
                                  List<ReservationItemResponseDTO> items,
                                  Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public EventResponseDTO getEvent() {
        return event;
    }

    public void setEvent(EventResponseDTO event) {
        this.event = event;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ReservationItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<ReservationItemResponseDTO> items) {
        this.items = items;
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