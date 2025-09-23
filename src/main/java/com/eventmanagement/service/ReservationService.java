package com.eventmanagement.service;

import com.eventmanagement.dto.ReservationItemRequestDTO;
import com.eventmanagement.dto.ReservationRequestDTO;
import com.eventmanagement.dto.ReservationResponseDTO;
import com.eventmanagement.entity.*;
import com.eventmanagement.mapper.ReservationMapper;
import com.eventmanagement.repository.EventRepository;
import com.eventmanagement.repository.ReservationRepository;
import com.eventmanagement.repository.TicketTypeRepository;
import com.eventmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private TicketTypeRepository ticketTypeRepository;
    private ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.reservationMapper = reservationMapper;
    }

    public ReservationResponseDTO createReservation(ReservationRequestDTO dto, Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + userId));
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Événement introuvable: " + dto.getEventId()));
        if (event.getStartsAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Impossible de réserver pour un événement passé");
        }

        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setEvent(event);
        reservation.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        List<ReservationItem> items = reservationMapper.itemDTOsToEntities(dto.getItems());

        for(int i=0; i<items.size(); i++){
            ReservationItem item = items.get(i);
            ReservationItemRequestDTO itemDTO = dto.getItems().get(i);

            TicketType tt = ticketTypeRepository.findById(itemDTO.getTicketTypeId())
                    .orElseThrow(()-> new RuntimeException("Type de billet introuvable: "+itemDTO.getTicketTypeId()));

            if(!tt.getEvent().getId().equals(event.getId())){
                throw new IllegalArgumentException("Le type de billet n'appartient pas à cet évenement");
            }
                        if(tt.getStock() < itemDTO.getQuantity()){
                throw new IllegalArgumentException("Stock insuffisant pour: "+tt.getName());
            }

            item.setReservation(reservation);
            item.setTicketType(tt);
            item.setUnitPrice(tt.getPrice());

            total = total.add(tt.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        reservation.setItems(items);
        reservation.setTotalAmount(total);
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationMapper.toResponseDTO(reservationRepository.save(reservation));
    }

    @Transactional()
    public ReservationResponseDTO findById(Long id){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Reservation introuvable: "+id));
        return reservationMapper.toResponseDTO(reservation);
    }

    @Transactional
    public Page<ReservationResponseDTO> findByUserId(Long userId, Pageable pageable){

        return reservationRepository.findByUserId(userId, pageable).map(reservationMapper::toResponseDTO);

    }

    @Transactional
    public Page<ReservationResponseDTO> findByStatus(ReservationStatus status, Pageable pageable) {
        return reservationRepository.findByStatus(status, pageable).map(reservationMapper::toResponseDTO);
    }

    public ReservationResponseDTO confirmReservation(Long id, Long userId){
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable: " + id));
        if (!reservation.getUser().getId().equals(userId)) {
            throw new RuntimeException("Non autorisé");
        }
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Seules les réservations en attente peuvent être confirmées");
        }
        if (reservation.getEvent().getStartsAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Événement passé");
        }

        for (ReservationItem item : reservation.getItems()) {
            if (item.getTicketType().getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Stock insuffisant pour: " + item.getTicketType().getName());
            }
        }
        reservation.setStatus(ReservationStatus.PAID);
        for (ReservationItem item : reservation.getItems()) {
            TicketType tt = item.getTicketType();
            tt.setStock(tt.getStock() - item.getQuantity());
            ticketTypeRepository.save(tt);
        }
        return reservationMapper.toResponseDTO(reservationRepository.save(reservation));
    }

    @Scheduled(fixedRate = 300000)
    public void cancelExpiredReservations() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<Reservation> expired = reservationRepository.findExpiredPendingReservations(oneHourAgo);
        for (Reservation r : expired) {
            r.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(r);
        }
    }


    public void cancelReservation(Long id, Long userId) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Reservation introuvable"));

        if (!userId.equals(reservation.getUser().getId())){
            throw new RuntimeException("Non autorisé");
        }

        reservationRepository.delete(reservation);

    }
}

