package com.eventmanagement.repository;

import com.eventmanagement.dto.EventResponseDTO;
import com.eventmanagement.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByCityIgnoreCase(String city, Pageable pageable);

    Page<Event> findByStartsAtAfter(LocalDateTime now, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Event> searchEvents(@Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Event> findByCreatedById(Long userId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.startsAt BETWEEN :startDate AND :endDate")
    Page<Event> findEventsByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate,
                                      Pageable pageable);

    Page<Event> findByCity(String city, Pageable pageable);

}
