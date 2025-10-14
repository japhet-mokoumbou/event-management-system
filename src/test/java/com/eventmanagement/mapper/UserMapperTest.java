// src/test/java/com/eventmanagement/mapper/UserMapperTest.java
package com.eventmanagement.mapper;

import com.eventmanagement.dto.UserRequestDTO;
import com.eventmanagement.dto.UserResponseDTO;
import com.eventmanagement.entity.Role;
import com.eventmanagement.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Tests du mapper UserMapper")
class UserMapperTest {

    private UserMapper userMapper;
    private UserRequestDTO userRequestDTO;
    private User user;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setFullname("John Doe");
        userRequestDTO.setPassword("password123");

        adminRole = new Role();
        adminRole.setName("ADMIN");

        userRole = new Role();
        userRole.setName("USER");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFullname("John Doe");
        user.setPasswordHash("hashedPassword");
        user.setEnabled(true);
        user.setRoles(Set.of(adminRole, userRole));
    }

    @Test
    @DisplayName("Devrait mapper UserRequestDTO vers User")
    void shouldMapUserRequestDTOToUser() {
        // Act
        User result = userMapper.toEntity(userRequestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(userRequestDTO.getEmail());
        assertThat(result.getFullname()).isEqualTo(userRequestDTO.getFullname());
        assertThat(result.getId()).isNull(); // ID ignoré
        assertThat(result.getPasswordHash()).isNull(); // PasswordHash ignoré
        assertThat(result.getRoles()).isNull(); // Roles ignoré
        assertThat(result.isEnabled()).isFalse(); // Enabled ignoré
    }

    @Test
    @DisplayName("Devrait mapper User vers UserResponseDTO")
    void shouldMapUserToUserResponseDTO() {
        // Act
        UserResponseDTO result = userMapper.toResponseDTO(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getFullname()).isEqualTo(user.getFullname());
        assertThat(result.getRoles()).containsExactlyInAnyOrder("ADMIN", "USER");
    }

    @Test
    @DisplayName("Devrait mapper une liste d'utilisateurs")
    void shouldMapUserList() {
        // Arrange
        java.util.List<User> users = java.util.List.of(user);

        // Act
        java.util.List<UserResponseDTO> result = userMapper.toResponseDTOList(users);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(user.getId());
        assertThat(result.get(0).getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Devrait mapper les rôles en chaînes")
    void shouldMapRolesToStrings() {
        // Act
        Set<String> result = userMapper.rolesToStrings(user.getRoles());

        // Assert
        assertThat(result).containsExactlyInAnyOrder("ADMIN", "USER");
    }

    @Test
    @DisplayName("Devrait gérer les rôles null")
    void shouldHandleNullRoles() {
        // Arrange
        user.setRoles(null);

        // Act
        Set<String> result = userMapper.rolesToStrings(user.getRoles());

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Devrait mettre à jour un utilisateur existant")
    void shouldUpdateExistingUser() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("old@example.com");
        existingUser.setFullname("Old");

        // Act
        userMapper.updateEntity(userRequestDTO, existingUser);

        // Assert
        assertThat(existingUser.getEmail()).isEqualTo(userRequestDTO.getEmail());
        assertThat(existingUser.getFullname()).isEqualTo(userRequestDTO.getFullname());
        assertThat(existingUser.getId()).isEqualTo(1L); // ID préservé
    }
}