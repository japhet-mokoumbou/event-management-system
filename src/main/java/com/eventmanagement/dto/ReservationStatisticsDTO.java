package com.eventmanagement.dto;

import java.math.BigDecimal;

public class ReservationStatisticsDTO {
    private long totalReservations;
    private long pendingReservations;
    private long paidReservations;
    private long cancelledReservations;
    private BigDecimal totalRevenue;

    public ReservationStatisticsDTO() {}

    public ReservationStatisticsDTO(long totalReservations, long pendingReservations,
                                    long paidReservations, long cancelledReservations,
                                    BigDecimal totalRevenue) {
        this.totalReservations = totalReservations;
        this.pendingReservations = pendingReservations;
        this.paidReservations = paidReservations;
        this.cancelledReservations = cancelledReservations;
        this.totalRevenue = totalRevenue;
    }
    // Getters et Setters
    public long getTotalReservations() { return totalReservations; }
    public void setTotalReservations(long totalReservations) { this.totalReservations = totalReservations; }

    public long getPendingReservations() { return pendingReservations; }
    public void setPendingReservations(long pendingReservations) { this.pendingReservations = pendingReservations; }

    public long getPaidReservations() { return paidReservations; }
    public void setPaidReservations(long paidReservations) { this.paidReservations = paidReservations; }

    public long getCancelledReservations() { return cancelledReservations; }
    public void setCancelledReservations(long cancelledReservations) { this.cancelledReservations = cancelledReservations; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}
