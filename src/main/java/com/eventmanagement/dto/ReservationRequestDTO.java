package com.eventmanagement.dto;

import com.eventmanagement.validation.ValidReservation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@ValidReservation
public class ReservationRequestDTO {

    @NotNull(message = "L'evenement est obligatoire")
    private Long eventId;

    @NotEmpty(message = "Au moins un type de billet doit être sélectionné")
    @Valid
    private List<ReservationItemRequestDTO> items;

    public ReservationRequestDTO() {
    }

    public ReservationRequestDTO(Long eventId, List<ReservationItemRequestDTO> items) {
        this.eventId = eventId;
        this.items = items;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<ReservationItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<ReservationItemRequestDTO> items) {
        this.items = items;
    }
}
