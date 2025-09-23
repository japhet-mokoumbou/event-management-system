package com.eventmanagement.controller;

import com.eventmanagement.dto.EventRequestDTO;
import com.eventmanagement.dto.EventResponseDTO;
import com.eventmanagement.dto.TicketTypeRequestDTO;
import com.eventmanagement.dto.TicketTypeResponseDTO;
import com.eventmanagement.service.EventService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
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

    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventResponseDTO>> getAllEvents(Pageable pageable){
        try{
            Page<EventResponseDTO> events = eventService.findAll(pageable);
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

    public ResponseEntity<Page<EventResponseDTO>> getEventsByUser(@PathVariable Long userId, Pageable pageable){
        try{
            Page<EventResponseDTO> events = eventService.findByUserId(userId, pageable);
            return ResponseEntity.ok(events);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequestDTO dto, @RequestParam Long userId){

        try{
            EventResponseDTO event = eventService.updateEvent(id,dto,userId);
            return ResponseEntity.ok(event);
        }catch (RuntimeException e){
            if (e.getMessage().contains("introuvable")){
                return ResponseEntity.notFound().build();
            }else if(e.getMessage().contains("Non autorisé")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.badRequest().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, @RequestParam Long userId){

        try{
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
                                                               @RequestParam Long userId){

        try{
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



}
