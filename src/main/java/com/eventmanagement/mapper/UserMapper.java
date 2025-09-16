package com.eventmanagement.mapper;

import com.eventmanagement.dto.EventRequestDTO;
import com.eventmanagement.dto.UserRequestDTO;
import com.eventmanagement.dto.UserResponseDTO;
import com.eventmanagement.entity.Event;
import com.eventmanagement.entity.Role;
import com.eventmanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convertit UserRequestDTO vers User
     * Note: Le mot de passe sera hashé dans le service
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    //Convertit User vers UserResponseDTO
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToString")
    UserResponseDTO toResponseDTO(User user);


    //Convertit une liste de User vers une liste de UserResponseDTO
    List<UserResponseDTO> toResponseDTOList(List<User> users);

    /**
     * Convertit un Set de Role vers un Set de String
     */
    default Set<String> rolesToStrings(Set<Role> roles){
        if(roles == null){
            return null;
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(UserRequestDTO userRequestDTO, @MappingTarget User user);

}
