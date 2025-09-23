package com.eventmanagement.service;

import com.eventmanagement.dto.EventRequestDTO;
import com.eventmanagement.dto.EventResponseDTO;
import com.eventmanagement.dto.TicketTypeRequestDTO;
import com.eventmanagement.dto.TicketTypeResponseDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.TicketType;
import com.eventmanagement.entity.User;
import com.eventmanagement.mapper.EventMapper;
import com.eventmanagement.mapper.TicketTypeMapper;
import com.eventmanagement.repository.EventRepository;
import com.eventmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    private EventRepository eventRepository;
    private EventMapper eventMapper;
    private UserRepository userRepository;
    private TicketType ticketType;
    private TicketTypeMapper ticketTypeMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper, UserRepository userRepository, TicketType ticketType, TicketTypeMapper ticketTypeMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.ticketType = ticketType;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    public EventResponseDTO createEvent(EventRequestDTO dto, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("Utilisateur introuvable: "+userId));
        if(dto.getEndsAt().isBefore(dto.getStartsAt())){
            throw new  RuntimeException("La date de fin doit être après la date de début");
        }

        if(dto.getStartsAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("La date de début doit être dans le futur");
        }

        Event event = eventMapper.toEntity(dto);
        event.setCreatedBy(user);
        return eventMapper.toResponseDTO(eventRepository.save(event));
    }

    @Transactional
    public EventResponseDTO findById(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Evenement introuvale: "+id));
        return eventMapper.toResponseDTO(event);
    }

    @Transactional
    public Page<EventResponseDTO> findAll(Pageable pageable){
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(eventMapper::toResponseDTO);
    }

    public Page<EventResponseDTO> findUpcoming(Pageable pageable){
        return eventRepository.findByStartsAtAfter(LocalDateTime.now(), pageable)
                .map(eventMapper::toResponseDTO);
    }

    public EventResponseDTO updateEvent(Long id, EventRequestDTO eventRequestDTO, Long userId){
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Evenement introuvale: "+id));
        if(!event.getCreatedBy().getId().equals(userId)){
            throw new RuntimeException("Non autorisé");
        }
        if(eventRequestDTO.getEndsAt().isBefore(eventRequestDTO.getStartsAt())){
            throw new  RuntimeException("La date de fin doit être après la date de début");
        }
        eventMapper.updateEntity(eventRequestDTO, event);
        return eventMapper.toResponseDTO(eventRepository.save(event));
    }

    public void deleteEvent(Long id, Long userId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Événement introuvable: " + id));
        if (!event.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Non autorisé");
        }
        eventRepository.delete(event);
    }

    public TicketTypeResponseDTO addTicketType(Long eventId, TicketTypeRequestDTO dto, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable: " + eventId));
        if (!event.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Non autorisé");
        }
        TicketType tt = ticketTypeMapper.toEntity(dto);
        tt.setEvent(event);
        event.getTicketTypes().add(tt);
        Event saved = eventRepository.save(event);
        TicketType persisted = saved.getTicketTypes().get(saved.getTicketTypes().size() - 1);
        return ticketTypeMapper.toResponseDTO(persisted);
    }

    public Page<EventResponseDTO> searchEvents(String q, Pageable pageable) {

        Page<Event> eventResponseDTOs = eventRepository.searchEvents(q,pageable);

        return eventResponseDTOs.map(eventMapper::toResponseDTO);
    }

    public Page<EventResponseDTO> findByCity(String city, Pageable pageable) {

        Page<Event> events = eventRepository.findByCity(city, pageable);
        return events.map(eventMapper::toResponseDTO);
    }

    public Page<EventResponseDTO> findByUserId(Long userId, Pageable pageable) {

        Page<Event> events = eventRepository.findByCreatedById(userId, pageable);
        return events.map(eventMapper::toResponseDTO);
    }
}
