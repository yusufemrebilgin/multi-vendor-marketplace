package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidIdentityNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentityNumberTest {

    @Nested
    @DisplayName("Valid identity number tests")
    class ValidIdentityNumbers {

        @Test
        @DisplayName("Should create identity number when valid")
        void shouldCreateWhenValid() {
            IdentityNumber id = new IdentityNumber("12345678901");
            assertEquals("12345678901", id.value());
        }

    }

    @Nested
    @DisplayName("Invalid identity number tests")
    class InvalidIdentityNumbers {

        @Test
        @DisplayName("Should throw exception when null")
        void shouldThrowWhenNull() {
            InvalidIdentityNumberException exception = assertThrows(
                    InvalidIdentityNumberException.class,
                    () -> new IdentityNumber(null)
            );
            assertEquals("Identity number must be exactly 11 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when empty")
        void shouldThrowWhenEmpty() {
            InvalidIdentityNumberException exception = assertThrows(
                    InvalidIdentityNumberException.class,
                    () -> new IdentityNumber("")
            );
            assertEquals("Identity number must be exactly 11 digits", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when not 11 digits")
        void shouldThrowWhenNot11Digits() {
            assertThrows(InvalidIdentityNumberException.class, () -> new IdentityNumber("123"));          // too short
            assertThrows(InvalidIdentityNumberException.class, () -> new IdentityNumber("123456789012")); // too long
            assertThrows(InvalidIdentityNumberException.class, () -> new IdentityNumber("1234567890"));   // 10 digits
        }

        @Test
        @DisplayName("Should throw exception when contains non-digit characters")
        void shouldThrowWhenContainsNonDigits() {
            assertThrows(InvalidIdentityNumberException.class, () -> new IdentityNumber("1234567890A"));
            assertThrows(InvalidIdentityNumberException.class, () -> new IdentityNumber("abcdefghijk"));
            assertThrows(InvalidIdentityNumberException.class, () -> new IdentityNumber("12345-78901"));
        }

    }

}
