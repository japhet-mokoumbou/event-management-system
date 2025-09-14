package com.eventmanagement.mapper;

import com.eventmanagement.dto.TicketTypeRequestDTO;
import com.eventmanagement.dto.TicketTypeResponseDTO;
import com.eventmanagement.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true) // Sera défini dans le service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TicketType toEntity(TicketTypeRequestDTO ticketTypeRequestDTO);


    //  Convertit TicketType vers TicketTypeResponseDTO
    TicketTypeResponseDTO toResponseDTO(TicketType ticketType);

    // Convertit une liste de TicketType vers une liste de TicketTypeResponseDTO
    List<TicketTypeResponseDTO> toResponseDTOList(List<TicketType> ticketTypes);


    // Met à jour un TicketType existant avec les données d'un TicketTypeRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(TicketTypeRequestDTO ticketTypeRequestDTO, @org.mapstruct.MappingTarget TicketType ticketType);

}
