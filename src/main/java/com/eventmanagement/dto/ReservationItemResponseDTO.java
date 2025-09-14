package com.eventmanagement.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class ReservationItemResponseDTO {

    private Long id;
    private TicketTypeResponseDTO ticketType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Instant createdAt;
    private Instant updatedAt;

    // Constructeurs
    public ReservationItemResponseDTO() {}

    public ReservationItemResponseDTO(Long id, TicketTypeResponseDTO ticketType,
                                      Integer quantity, BigDecimal unitPrice,
                                      Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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

    public TicketTypeResponseDTO getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketTypeResponseDTO ticketType) {
        this.ticketType = ticketType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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