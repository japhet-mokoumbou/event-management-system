package com.eventmanagement.repository;

import com.eventmanagement.entity.Reservation;
import com.eventmanagement.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
}
