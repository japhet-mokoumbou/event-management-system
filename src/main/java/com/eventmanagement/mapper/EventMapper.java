package com.eventmanagement.mapper;

import com.eventmanagement.dto.EventRequestDTO;
import com.eventmanagement.dto.EventResponseDTO;
import com.eventmanagement.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TicketTypeMapper.class})
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toEntity(EventRequestDTO eventRequestDTO);

    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "ticketTypes", source = "ticketTypes")
    EventResponseDTO toResponseDTO(Event event);

    List<EventResponseDTO> toResponseDTOList(List<Event> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(EventRequestDTO eventRequestDTO, @MappingTarget Event event);
}
