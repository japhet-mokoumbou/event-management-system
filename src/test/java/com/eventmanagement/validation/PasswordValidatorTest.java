// src/test/java/com/eventmanagement/validation/PasswordValidatorTest.java
package com.eventmanagement.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Tests du validateur PasswordValidator")
class PasswordValidatorTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    @DisplayName("Devrait valider un mot de passe valide")
    void shouldValidateValidPassword() {
        // Act & Assert
        assertThat(passwordValidator.isValid("Password123!", null)).isTrue();
        assertThat(passwordValidator.isValid("MySecure1@", null)).isTrue();
        assertThat(passwordValidator.isValid("ComplexP@ss1", null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "password", // Pas de majuscule, chiffre, caractère spécial
            "PASSWORD", // Pas de minuscule, chiffre, caractère spécial
            "Password", // Pas de chiffre, caractère spécial
            "Password1", // Pas de caractère spécial
            "Pass1!", // Trop court
            "", // Vide
            "12345678", // Pas de lettre
            "Password!", // Pas de chiffre
            "Password1" // Pas de caractère spécial
    })
    @DisplayName("Devrait rejeter des mots de passe invalides")
    void shouldRejectInvalidPasswords(String invalidPassword) {
        // Act & Assert
        assertThat(passwordValidator.isValid(invalidPassword, null)).isFalse();
    }

    @Test
    @DisplayName("Devrait accepter null comme mot de passe valide")
    void shouldAcceptNullAsValidPassword() {
        // Act & Assert
        assertThat(passwordValidator.isValid(null, null)).isTrue();
    }
}