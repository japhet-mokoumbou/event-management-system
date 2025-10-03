package com.eventmanagement.controller;

import com.eventmanagement.dto.ReservationRequestDTO;
import com.eventmanagement.dto.ReservationResponseDTO;
import com.eventmanagement.dto.ReservationStatisticsDTO;
import com.eventmanagement.entity.ReservationStatus;
import com.eventmanagement.entity.User;
import com.eventmanagement.repository.UserRepository;
import com.eventmanagement.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = {"http://localhos:3000, http://localhost:8080"})
public class ReservationController {

    private ReservationService reservationService;
    private UserRepository userRepository;

    public ReservationController(ReservationService reservationService, UserRepository userRepository){
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(@Valid @RequestBody ReservationRequestDTO reservationRequestDTO, Authentication authentication){
        try{
            Long userId = currentUserId(authentication);
            ReservationResponseDTO reservationResponseDTO = reservationService.createReservation(reservationRequestDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id){

        try{
            ReservationResponseDTO responseDTO = reservationService.findById(id);
            return ResponseEntity.ok(responseDTO);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Trouve les réservations d'un utilisateur
     * GET /api/reservations/user/{userId}?page=0&size=10
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReservationResponseDTO>> getReservationsByUser(@PathVariable Long userId, Pageable pageable) {
        try {
            Page<ReservationResponseDTO> reservations = reservationService.findByUserId(userId, pageable);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReservationResponseDTO>> getReservationsByStatus(@PathVariable ReservationStatus status, Pageable pageable) {
        try {
            Page<ReservationResponseDTO> reservations = reservationService.findByStatus(status, pageable);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ReservationResponseDTO> confirmReservation(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = currentUserId(authentication);
            ReservationResponseDTO reservation = reservationService.confirmReservation(id, userId);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("introuvable")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Non autorisé")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = currentUserId(authentication);
            reservationService.cancelReservation(id, userId);
            return ResponseEntity.accepted().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("introuvable")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Non autorisé")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Long currentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return user.getId();
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<Page<ReservationResponseDTO>> getMyReservations(
            Authentication authentication, Pageable pageable) {
        try {
            Long userId = currentUserId(authentication);
            Page<ReservationResponseDTO> reservations = reservationService.findByUserId(userId, pageable);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Page<ReservationResponseDTO>> getReservationsByEvent(
            @PathVariable Long eventId, Pageable pageable) {
        try {
            Page<ReservationResponseDTO> reservations = reservationService.findByEventId(eventId, pageable);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<ReservationStatisticsDTO> getReservationStatistics() {
        try {
            ReservationStatisticsDTO statistics = reservationService.getReservationStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
