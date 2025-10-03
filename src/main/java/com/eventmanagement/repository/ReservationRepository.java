package com.eventmanagement.repository;

import com.eventmanagement.entity.Reservation;
import com.eventmanagement.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByUserId(Long userId, Pageable pageable);

    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

    Page<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status, Pageable pageable);

    /**
     * Trouve les réservations en attente depuis plus d'une heure
     * @param oneHourAgo date d'il y a une heure
     * @return List<Reservation> liste des réservations expirées
     */

    @Query("SELECT r FROM Reservation r WHERE r.status = 'PENDING' AND r.createdAt< :oneHourAgo")
    List<Reservation> findExpiredPendingReservations(@Param("oneHourAgo")LocalDateTime oneHourAgo);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.event.id = :eventId")
    long countByEventId(@Param("eventId") Long eventId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.event.id = :eventId AND r.status = :status")
    long countByEventIdAndStatus(@Param("eventId") Long eventId, @Param("status") ReservationStatus status);

    @Query("SELECT COALESCE(SUM(ri.quantity), 0) " +
            "FROM ReservationItem ri " +
            "WHERE ri.reservation.event.id = :eventId AND ri.reservation.status = com.eventmanagement.entity.ReservationStatus.PAID")
    long sumTicketsSoldByEventId(@Param("eventId") Long eventId);

    @Query("SELECT COALESCE(SUM(r.totalAmount), 0) " +
            "FROM Reservation r " +
            "WHERE r.event.id = :eventId AND r.status = com.eventmanagement.entity.ReservationStatus.PAID")
    java.math.BigDecimal sumRevenueByEventId(@Param("eventId") Long eventId);

    Page<Reservation> findByEventId(Long eventId, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Reservation r")
    long count();

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = :status")
    long countByStatus(@Param("status") ReservationStatus status);

    @Query("SELECT COALESCE(SUM(r.totalAmount), 0) FROM Reservation r WHERE r.status = :status")
    BigDecimal sumTotalAmountByStatus(@Param("status") ReservationStatus status);
}
