package com.yusufemrebilgin.marketplace.auth.domain.model;

import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidUsernameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {

    @Nested
    @DisplayName("Valid username tests")
    class ValidUsernames {

        @Test
        @DisplayName("Should create username when valid")
        void shouldCreateWhenValid() {
            Username username = new Username("m_scott-123");
            assertEquals("m_scott-123", username.value());
        }

        @Test
        @DisplayName("Should normalize to lowercase and trim whitespace")
        void shouldNormalizeToLowercaseAndTrim() {
            Username username = new Username("  Jim.Halpert ");
            assertEquals("jim.halpert", username.value());
        }

        @Test
        @DisplayName("Should allow min and max length")
        void shouldAllowMinAndMaxLength() {
            Username min = new Username("abc");             // 3 chars
            assertEquals("abc", min.value());

            Username max = new Username("a".repeat(20));    // 20 chars
            assertEquals("a".repeat(20), max.value());
        }
    }

    @Nested
    @DisplayName("Invalid username tests")
    class InvalidUsernames {

        @Test
        @DisplayName("Should throw for null")
        void shouldThrowForNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Username(null)
            );
            assertEquals("Username cannot be null or empty", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw for empty or whitespace only")
        void shouldThrowForEmptyOrWhitespace() {
            IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> new Username(""));
            assertEquals("Username cannot be null or empty", ex1.getMessage());

            IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> new Username("   "));
            assertEquals("Username cannot be null or empty", ex2.getMessage());
        }

        @Test
        @DisplayName("Should throw for length less than 3")
        void shouldThrowForTooShort() {
            InvalidUsernameException ex = assertThrows(
                    InvalidUsernameException.class,
                    () -> new Username("ab")
            );
            assertEquals("Username must be between 3 and 20 characters", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw for length greater than 20")
        void shouldThrowForTooLong() {
            String tooLong = "a".repeat(21);
            InvalidUsernameException ex = assertThrows(
                    InvalidUsernameException.class,
                    () -> new Username(tooLong)
            );
            assertEquals("Username must be between 3 and 20 characters", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw for invalid characters")
        void shouldThrowForInvalidCharacters() {
            assertThrows(InvalidUsernameException.class, () -> new Username("jim!Halpert"));
            assertThrows(InvalidUsernameException.class, () -> new Username("jim@halpert"));
            assertThrows(InvalidUsernameException.class, () -> new Username("jim/halpert"));
            assertThrows(InvalidUsernameException.class, () -> new Username("jim halpert"));
        }
    }

}