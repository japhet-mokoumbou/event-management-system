package com.eventmanagement.mapper;

import com.eventmanagement.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default String roleToString(Role role){
        if(role == null){
            return null;
        }
        return role.getName();
    }

    default Role stringToRole(String roleName){
        if(roleName == null){
            return null;
        }

        return new Role(roleName);
    }
}
