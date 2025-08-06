package com.yusufemrebilgin.marketplace.auth.domain.model;

import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Nested
    @DisplayName("Valid password tests")
    class ValidPasswords {

        @Test
        @DisplayName("Should create password when valid")
        void shouldCreatePasswordWhenValid() {
            Password password = new Password("s3cr3t1234");
            assertEquals("s3cr3t1234", password.raw());
            assertEquals("************", password.toString());
        }

        @Test
        @DisplayName("Should allow min length password")
        void shouldAllowMinLengthPassword() {
            Password password = new Password("Abc12345"); // exactly 8 chars
            assertEquals("Abc12345", password.raw());
        }

        @Test
        @DisplayName("Should allow max length password")
        void shouldAllowMaxLengthPassword() {
            String maxLengthPassword = "A1" + "x".repeat(30); // total 32 chars
            Password password = new Password(maxLengthPassword);
            assertEquals(maxLengthPassword, password.raw());
        }

    }

    @Nested
    @DisplayName("Invalid password tests")
    class InvalidPasswords {

        @Test
        @DisplayName("Should throw exception for null password")
        void shouldThrowForNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Password(null)
            );
            assertEquals("Password cannot be null or empty", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty password")
        void shouldThrowForEmpty() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Password("   ")
            );
            assertEquals("Password cannot be null or empty", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for password shorter than min length")
        void shouldThrowForTooShortPassword() {
            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> new Password("Abc123")
            );
            assertEquals("Password must be between 8 and 32 characters", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for password longer than max length")
        void shouldThrowForTooLongPassword() {
            String tooLongPassword = "A1" + "x".repeat(31); // 33 chars
            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> new Password(tooLongPassword)
            );
            assertEquals("Password must be between 8 and 32 characters", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for password with no digits")
        void shouldThrowForNoDigits() {
            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> new Password("OnlyLetters")
            );
            assertEquals("Password must contain both letters and digits", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for password with no letters")
        void shouldThrowForNoLetters() {
            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> new Password("12345678")
            );
            assertEquals("Password must contain both letters and digits", ex.getMessage());
        }

    }

}