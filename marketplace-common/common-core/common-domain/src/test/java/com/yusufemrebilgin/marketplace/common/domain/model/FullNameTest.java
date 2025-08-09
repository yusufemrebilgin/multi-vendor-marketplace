package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidFullNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    @Nested
    @DisplayName("Valid full name tests")
    class ValidFullNames {

        @Test
        @DisplayName("Should create full name successfully with first and last name")
        void shouldCreateWithFirstAndLastName() {
            FullName name = new FullName("Hayko", "C");
            assertEquals("Hayko", name.firstName());
            assertEquals("C", name.lastName());
            assertEquals("Hayko C", name.toString());
        }

        @Test
        @DisplayName("Should trim both first and last names")
        void shouldTrimBothNames() {
            FullName name = new FullName("  Hayko  ", "  C  ");
            assertEquals("Hayko", name.firstName());
            assertEquals("C", name.lastName());
            assertEquals("Hayko C", name.toString());
        }

        @Test
        @DisplayName("Should allow null last name and keep first name")
        void shouldAllowNullLastName() {
            FullName name = new FullName("Hayko", null);
            assertEquals("Hayko", name.firstName());
            assertNull(name.lastName());
            assertEquals("Hayko", name.toString());
        }

        @Test
        @DisplayName("Should convert blank last name to null after trimming")
        void shouldConvertBlankLastNameToNull() {
            FullName withEmpty = new FullName("Hayko", "");
            assertEquals("Hayko", withEmpty.firstName());
            assertNull(withEmpty.lastName());
            assertEquals("Hayko", withEmpty.toString());

            FullName withBlanks = new FullName("Hayko", "   ");
            assertEquals("Hayko", withBlanks.firstName());
            assertNull(withBlanks.lastName());
            assertEquals("Hayko", withBlanks.toString());
        }

    }

    @Nested
    @DisplayName("Invalid full name tests")
    class InvalidFullNames {

        @Test
        @DisplayName("Should throw exception for null first name")
        void shouldThrowForNullFirstName() {
            InvalidFullNameException ex = assertThrows(
                    InvalidFullNameException.class,
                    () -> new FullName(null, "C")
            );
            assertEquals("First name cannot be null or empty", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty first name")
        void shouldThrowForEmptyFirstName() {
            InvalidFullNameException ex = assertThrows(
                    InvalidFullNameException.class,
                    () -> new FullName("", "C")
            );
            assertEquals("First name cannot be null or empty", ex.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for blank first name")
        void shouldThrowForBlankFirstName() {
            InvalidFullNameException ex = assertThrows(
                    InvalidFullNameException.class,
                    () -> new FullName("   ", "C")
            );
            assertEquals("First name cannot be null or empty", ex.getMessage());
        }

    }

}
