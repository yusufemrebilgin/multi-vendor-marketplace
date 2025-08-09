package com.yusufemrebilgin.marketplace.auth.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenPairTest {

    @Nested
    @DisplayName("Valid token pair tests")
    class ValidTokenPairs {

        @Test
        @DisplayName("Should create token pair successfully with valid values")
        void shouldCreateWithValidValues() {
            TokenPair pair = new TokenPair("access-token", "refresh-token", 3600L);
            assertEquals("access-token", pair.accessToken());
            assertEquals("refresh-token", pair.refreshToken());
            assertEquals(3600L, pair.expiresInMs());
        }

        @Test
        @DisplayName("Should trim whitespace from tokens")
        void shouldTrimWhitespace() {
            TokenPair pair = new TokenPair("  access  ", "  refresh  ", 1000L);
            assertEquals("access", pair.accessToken());
            assertEquals("refresh", pair.refreshToken());
        }

    }

    @Nested
    @DisplayName("Invalid token pair tests")
    class InvalidTokenPairs {

        @Test
        @DisplayName("Should throw when access token is null")
        void shouldThrowWhenAccessNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair(null, "refresh", 1000L)
            );
            assertEquals("Access token cannot be null or empty", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw when access token is empty or blank")
        void shouldThrowWhenAccessEmptyOrBlank() {
            IllegalArgumentException ex1 = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair("", "refresh", 1000L)
            );
            assertEquals("Access token cannot be null or empty", ex1.getMessage());

            IllegalArgumentException ex2 = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair("   ", "refresh", 1000L)
            );
            assertEquals("Access token cannot be null or empty", ex2.getMessage());
        }

        @Test
        @DisplayName("Should throw when refresh token is null")
        void shouldThrowWhenRefreshNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair("access", null, 1000L)
            );
            assertEquals("Refresh token cannot be null or empty", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw when refresh token is empty or blank")
        void shouldThrowWhenRefreshEmptyOrBlank() {
            IllegalArgumentException ex1 = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair("access", "", 1000L)
            );
            assertEquals("Refresh token cannot be null or empty", ex1.getMessage());

            IllegalArgumentException ex2 = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair("access", "   ", 1000L)
            );
            assertEquals("Refresh token cannot be null or empty", ex2.getMessage());
        }

        @Test
        @DisplayName("Should throw when expiration is zero or negative")
        void shouldThrowWhenExpirationNonPositive() {
            IllegalArgumentException ex1 = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair("access", "refresh", 0L)
            );
            assertEquals("Expiration must be positive", ex1.getMessage());

            IllegalArgumentException ex2 = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TokenPair("access", "refresh", -1L)
            );
            assertEquals("Expiration must be positive", ex2.getMessage());
        }

    }


}
