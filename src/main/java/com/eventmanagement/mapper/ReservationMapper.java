package com.eventmanagement.mapper;

import com.eventmanagement.dto.ReservationItemRequestDTO;
import com.eventmanagement.dto.ReservationItemResponseDTO;
import com.eventmanagement.dto.ReservationRequestDTO;
import com.eventmanagement.dto.ReservationResponseDTO;
import com.eventmanagement.entity.Reservation;
import com.eventmanagement.entity.ReservationItem;
import com.eventmanagement.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {UserMapper.class, EventMapper.class, TicketTypeMapper.class})
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // Sera défini dans le service
    @Mapping(target = "event", ignore = true) // Sera défini dans le service
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "totalAmount", constant = "0.0")
    @Mapping(target = "items", source = "items", qualifiedByName = "itemDTOsToEntities")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reservation toEntity(ReservationRequestDTO reservationRequestDTO);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "event", source = "event")
    @Mapping(target = "items", source = "items")
    ReservationResponseDTO toResponseDTO(Reservation reservation);

    List<ReservationResponseDTO> toResponseDTOList(List<Reservation> reservations);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservation", ignore = true) // Sera défini dans le service
    @Mapping(target = "ticketType", source = "ticketTypeId", qualifiedByName = "ticketTypeIdToEntity")
    @Mapping(target = "unitPrice", ignore = true) // Sera calculé dans le service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReservationItem itemDTOToEntity(ReservationItemRequestDTO itemDTO);

    @Mapping(target = "ticketType", source = "ticketType")
    ReservationItemResponseDTO itemToResponseDTO(ReservationItem item);

    @Named("itemDTOsToEntities")
    default List<ReservationItem> itemDTOsToEntities(List<ReservationItemRequestDTO> itemDTOs) {
        if (itemDTOs == null) {
            return null;
        }
        return itemDTOs.stream()
                .map(this::itemDTOToEntity)
                .collect(java.util.stream.Collectors.toList());
    }

    @Named("ticketTypeIdToEntity")
    default TicketType ticketTypeIdToEntity(Long ticketTypeId) {
        if (ticketTypeId == null) {
            return null;
        }
        TicketType ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        return ticketType;
    }
}
