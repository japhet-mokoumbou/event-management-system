package com.eventmanagement.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Tests de l'entité User")
public class UserTest {

    private User user;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp(){
        user = new User();
        adminRole = new Role();
        adminRole.setName("ADMIN");
        userRole = new Role();
        userRole.setName("USER");
    }

    @Test
    @DisplayName("Devrait créer un utilisateur avec des données valides")
    void shouldCreateUserWithValidData(){
        // Arrange
        String email = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String passwordHash = "hashedPassword";

        // Act
        user.setEmail(email);
        user.setFullname(firstName+" "+lastName);
        user.setPasswordHash(passwordHash);
        user.setEnabled(true);

        // Assert
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getFullname()).isEqualTo(firstName+" "+lastName);
        assertThat(user.getPasswordHash()).isEqualTo(passwordHash);
        assertThat(user.isEnabled()).isTrue();
    }

    void shouldAssignRolesToUser(){

        // Arrange
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);

        //Act
        user.setRoles(roles);

        // Assert
        assertThat(user.getRoles()).hasSize(2);
        assertThat(user.getRoles()).contains(adminRole, userRole);
    }

    @Test
    @DisplayName("Devrait avoir un état activé par défaut")
    void shouldHaveEnabledStateByDefault() {
        // Assert
        assertThat(user.isEnabled()).isTrue();
    }
}
