package com.eventmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations",
        indexes = {
                @Index(name = "idx_reservations_user", columnList = "user_id"),
                @Index(name = "idx_reservations_event", columnList = "event_id")  // ← Ajout
        }
)
public class Reservation extends BaseEntity {  // ← CORRECTION 1

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservations_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservations_event")
    )
    private Event event;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationItem> items = new ArrayList<>();

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {  // ← CORRECTION 3 : Ajout du setter
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // ← CORRECTION 4 : Ajout des getters/setters pour Event
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
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

    public List<ReservationItem> getItems() {
        return items;
    }

    public void setItems(List<ReservationItem> items) {
        this.items = items;
    }
}