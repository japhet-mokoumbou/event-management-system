package com.eventmanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservationItemRequestDTO {

    @NotNull(message = "Le type de billet est obligatoire")
    private Long ticketTypeId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantity;

    public ReservationItemRequestDTO() {
    }

    public ReservationItemRequestDTO(Long ticketTypeId, Integer quantity) {
        this.ticketTypeId = ticketTypeId;
        this.quantity = quantity;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
