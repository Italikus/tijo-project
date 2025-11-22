package com.example.tijo.backend.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PeselValidatorTest {

    private PeselValidator peselValidator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        peselValidator = new PeselValidator();
    }

    @Test
    void isValid_shouldReturnTrue_forValidPesel() {
        // Given - poprawny PESEL: 44051401458
        String validPesel = "44051401458";

        // When
        boolean result = peselValidator.isValid(validPesel, context);

        // Then
        assertTrue(result);
    }

    @Test
    void isValid_shouldReturnTrue_forValidPeselWithWhitespace() {
        // Given - poprawny PESEL z białymi znakami
        String validPesel = "  44051401458  ";

        // When
        boolean result = peselValidator.isValid(validPesel, context);

        // Then
        assertTrue(result);
    }

    // Testy dla niepoprawnych numerów PESEL

    @Test
    void isValid_shouldReturnFalse_forPeselWithInvalidChecksum() {
        // Given - PESEL z błędną sumą kontrolną
        String invalidPesel = "44051401459";

        // When
        boolean result = peselValidator.isValid(invalidPesel, context);

        // Then
        assertFalse(result);
    }

    @Test
    void isValid_shouldReturnFalse_forNullOrEmptyPesel() {
        // Given & When & Then
        assertFalse(peselValidator.isValid(null, context));
        assertFalse(peselValidator.isValid("", context));
        assertFalse(peselValidator.isValid("   ", context));
    }

    @Test
    void isValid_shouldReturnFalse_forPeselWithInvalidLength() {
        // Given & When & Then
        assertFalse(peselValidator.isValid("4405140145", context)); // za krótki
        assertFalse(peselValidator.isValid("440514014588", context)); // za długi
    }

    @Test
    void isValid_shouldReturnFalse_forPeselWithInvalidCharacters() {
        // Given & When & Then
        assertFalse(peselValidator.isValid("4405140145A", context)); // litery
        assertFalse(peselValidator.isValid("44051401-58", context)); // znaki specjalne
        assertFalse(peselValidator.isValid("44051 401458", context)); // spacje w środku
    }
}

