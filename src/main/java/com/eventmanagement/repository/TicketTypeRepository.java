package com.eventmanagement.repository;

import com.eventmanagement.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    List<TicketType> findByEventId(Long eventId);

    @Query("SELECT t FROM TicketType t WHERE t.event.id = :eventId AND stock > 0")
    List<TicketType> findAvailableByEventId(@Param("eventId") Long eventId);

    @Query("SELECT CASE WHEN t.stock >= :quantity THEN true ELSE false END")
    boolean hasEnoughStock(@Param("ticketTypeId") Long ticketTypeId,
                           @Param("quantity") Integer quantity);
}
