package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidIdentifierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierTest {

    @Nested
    @DisplayName("Valid identifier tests")
    class ValidIdentifiers {

        @Test
        @DisplayName("Should create identifier successfully for valid UUID")
        void shouldCreateIdentifierForValidUuid() {
            String uuid = "123e4567-e89b-12d3-a456-426614174000";
            Identifier identifier = new Identifier(uuid);
            assertEquals(uuid, identifier.value());
        }

        @Test
        @DisplayName("Should trim whitespace from UUID input")
        void shouldTrimWhitespaceFromUuid() {
            Identifier identifier = new Identifier("  123e4567-e89b-12d3-a456-426614174000  ");
            assertEquals("123e4567-e89b-12d3-a456-426614174000", identifier.value());
        }

        @Test
        @DisplayName("Should generate valid unique UUID identifiers")
        void shouldGenerateValidUniqueUuids() {
            Identifier id1 = Identifier.generate();
            Identifier id2 = Identifier.generate();

            assertDoesNotThrow(() -> UUID.fromString(id1.value()));
            assertDoesNotThrow(() -> UUID.fromString(id2.value()));
            assertNotEquals(id1.value(), id2.value());
        }

    }

    @Nested
    @DisplayName("Invalid identifier tests")
    class InvalidIdentifiers {

        @Test
        @DisplayName("Should throw exception for null input")
        void shouldThrowExceptionForNull() {
            InvalidIdentifierException exception = assertThrows(
                    InvalidIdentifierException.class,
                    () -> new Identifier(null)
            );
            assertEquals("Identifier cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty input")
        void shouldThrowExceptionForEmpty() {
            InvalidIdentifierException exception = assertThrows(
                    InvalidIdentifierException.class,
                    () -> new Identifier("")
            );
            assertEquals("Identifier cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for blank input")
        void shouldThrowExceptionForBlank() {
            InvalidIdentifierException exception = assertThrows(
                    InvalidIdentifierException.class,
                    () -> new Identifier("   ")
            );
            assertEquals("Identifier cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for malformed UUID")
        void shouldThrowExceptionForMalformedUuid() {
            InvalidIdentifierException exception = assertThrows(
                    InvalidIdentifierException.class,
                    () -> new Identifier("not-a-uuid")
            );
            assertEquals("Invalid UUID format: 'not-a-uuid'", exception.getMessage());
        }

    }

}
