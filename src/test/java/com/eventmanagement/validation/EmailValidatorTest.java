// src/test/java/com/eventmanagement/validation/EmailValidatorTest.java
package com.eventmanagement.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.eventmanagement.repository.UserRepository;
import static org.mockito.Mockito.*;


import static org.assertj.core.api.Assertions.*;

@DisplayName("Tests du validateur EmailValidator")
class EmailValidatorTest {

    private EmailValidator emailValidator;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        emailValidator = new EmailValidator(userRepository);    }

    @Test
    @DisplayName("Devrait valider un email valide")
    void shouldValidateValidEmail() {
        // Act & Assert
        assertThat(emailValidator.isValid("test@example.com", null)).isTrue();
        assertThat(emailValidator.isValid("user.name@domain.co.uk", null)).isTrue();
        assertThat(emailValidator.isValid("admin+tag@company.org", null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid-email",
            "@example.com",
            "test@",
            "test..test@example.com",
            "",
            "test@.com",
            "test@com.",
            "test@.com."
    })
    @DisplayName("Devrait rejeter des emails invalides")
    void shouldRejectInvalidEmails(String invalidEmail) {
        // Act & Assert
        assertThat(emailValidator.isValid(invalidEmail, null)).isFalse();
    }

    @Test
    @DisplayName("Devrait accepter null comme email valide")
    void shouldAcceptNullAsValidEmail() {
        // Act & Assert
        assertThat(emailValidator.isValid(null, null)).isTrue();
    }
}