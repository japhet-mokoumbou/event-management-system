package com.eventmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventStatisticsDTO {

    private Long eventId;
    private String eventTitle;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private int totalTicketsSold;
    private int totalTicketsAvailable;
    private BigDecimal totalRevenue;
    private long totalReservations;
    private long activeReservations;
    private long cancelledReservations;

    public EventStatisticsDTO() {}

    public EventStatisticsDTO(Long eventId, String eventTitle, LocalDateTime startsAt, LocalDateTime endsAt,
                              int totalTicketsSold, int totalTicketsAvailable, BigDecimal totalRevenue,
                              long totalReservations, long activeReservations, long cancelledReservations) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.totalTicketsSold = totalTicketsSold;
        this.totalTicketsAvailable = totalTicketsAvailable;
        this.totalRevenue = totalRevenue;
        this.totalReservations = totalReservations;
        this.activeReservations = activeReservations;
        this.cancelledReservations = cancelledReservations;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
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

    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }

    public void setTotalTicketsSold(int totalTicketsSold) {
        this.totalTicketsSold = totalTicketsSold;
    }

    public int getTotalTicketsAvailable() {
        return totalTicketsAvailable;
    }

    public void setTotalTicketsAvailable(int totalTicketsAvailable) {
        this.totalTicketsAvailable = totalTicketsAvailable;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(long totalReservations) {
        this.totalReservations = totalReservations;
    }

    public long getActiveReservations() {
        return activeReservations;
    }

    public void setActiveReservations(long activeReservations) {
        this.activeReservations = activeReservations;
    }

    public long getCancelledReservations() {
        return cancelledReservations;
    }

    public void setCancelledReservations(long cancelledReservations) {
        this.cancelledReservations = cancelledReservations;
    }
}
