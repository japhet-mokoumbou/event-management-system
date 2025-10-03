package com.eventmanagement.controller;

import com.eventmanagement.dto.*;
import com.eventmanagement.entity.User;
import com.eventmanagement.repository.UserRepository;
import com.eventmanagement.service.EventService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class EventController {

    private final EventService eventService;
    private UserRepository userRepository;

    public EventController(EventService eventService, UserRepository userRepository) {

        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO eventRequestDTO, @RequestParam Long userId){

        try{
            EventResponseDTO event = eventService.createEvent(eventRequestDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(event);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id){
        try {
            EventResponseDTO eventResponseDTO = eventService.findById(id);
            return ResponseEntity.ok(eventResponseDTO);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> getAllEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false, name = "q") String search,
            Pageable pageable){

        try{
            Page<EventResponseDTO> events;
            if(city != null && !city.isEmpty()){
                events = eventService.findByCity(city, pageable);
                return ResponseEntity.ok(events);
            }else if (search != null && !search.isEmpty()) {
                events = eventService.searchEvents(search, pageable);
            } else {
                events = eventService.findAll(pageable);
            }
            return ResponseEntity.ok(events);

        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventResponseDTO>> getUpcomingEvents(Pageable pageable){
        try{
            Page<EventResponseDTO> events = eventService.findUpcoming(pageable);
            return ResponseEntity.ok(events);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EventResponseDTO>> searchEvents(@RequestParam String q, Pageable pageable){
        try{
            Page<EventResponseDTO> events = eventService.searchEvents(q,pageable);
            return ResponseEntity.ok(events);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/city/{city}")
    public ResponseEntity<Page<EventResponseDTO>> getEventByCity(@PathVariable String city, Pageable pageable){

        try{
            Page<EventResponseDTO> events = eventService.findByCity(city, pageable);
            return ResponseEntity.ok(events);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByUser(@PathVariable Long userId, Pageable pageable){
        try{
            Page<EventResponseDTO> events = eventService.findByUserId(userId, pageable);
            return ResponseEntity.ok(events);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO dto, Authentication authentication){

        try {
            Long userId = currentUserId(authentication);
            EventResponseDTO event = eventService.updateEvent(id, dto, userId);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, Authentication authentication){

        try{
            Long userId = currentUserId(authentication);
            eventService.deleteEvent(id, userId);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
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

    @PostMapping("/{eventId}/ticket-types")
    public ResponseEntity<TicketTypeResponseDTO> addTicketType(@PathVariable Long eventId,
                                                               @Valid @RequestBody TicketTypeRequestDTO ticketTypeRequestDTO,
                                                               Authentication authentication){

        try{
            Long userId = currentUserId(authentication);
            TicketTypeResponseDTO ticketType = eventService.addTicketType(eventId, ticketTypeRequestDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketType);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("introuvable")){
                return ResponseEntity.notFound().build();
            }
            else if (e.getMessage().contains("Non autorisé")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    private Long currentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        return user.getId();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelEvent(@PathVariable Long id, Authentication authentication){
        try{
            Long userId = currentUserId(authentication);
            eventService.cancelEvent(id, userId);
            return ResponseEntity.ok().build();
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<EventStatisticsDTO> getEventStatistics(@PathVariable Long id){
        try{
            var stats = eventService.getEventStatistics(id);
            return ResponseEntity.ok(stats);
        }catch(RuntimeException e){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/by-organizer/{organizerId}")
    public ResponseEntity<Page<EventResponseDTO>> getEventsByOrganizer(@PathVariable Long organizerId, Pageable pageable) {
        try {
            Page<EventResponseDTO> events = eventService.findByOrganizer(organizerId, pageable);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search/by-title")
    public ResponseEntity<Page<EventResponseDTO>> searchByTitle(@RequestParam String title, Pageable pageable) {
        try {
            Page<EventResponseDTO> events = eventService.findByTitleContaining(title, pageable);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
