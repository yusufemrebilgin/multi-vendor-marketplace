package com.yusufemrebilgin.marketplace.auth.domain.model;

import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Nested
    @DisplayName("Valid email tests")
    class ValidEmails {

        @Test
        @DisplayName("Should create email successfully for valid email")
        void shouldCreateEmailForValidInput() {
            Email email = new Email("mike@example.com");
            assertEquals("mike@example.com", email.value());
        }

        @Test
        @DisplayName("Should trim and lowercase input")
        void shouldTrimAndLowercaseInput() {
            Email email = new Email("  MichaelScott@Example.COM ");
            assertEquals("michaelscott@example.com", email.value());
        }

    }

    @Nested
    @DisplayName("Invalid email tests")
    class InvalidEmails {

        @Test
        @DisplayName("Should throw exception for null input")
        void shouldThrowExceptionForNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Email(null)
            );
            assertEquals("Email cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty input")
        void shouldThrowExceptionForEmpty() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Email("")
            );
            assertEquals("Email cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for malformed email")
        void shouldThrowExceptionForMalformedEmail() {
            assertThrows(InvalidEmailException.class, () -> new Email("invalid-email"));
            assertThrows(InvalidEmailException.class, () -> new Email("test@"));
            assertThrows(InvalidEmailException.class, () -> new Email("@example.com"));
            assertThrows(InvalidEmailException.class, () -> new Email("test@example"));
            assertThrows(InvalidEmailException.class, () -> new Email("test@example..com"));
        }

    }

}
