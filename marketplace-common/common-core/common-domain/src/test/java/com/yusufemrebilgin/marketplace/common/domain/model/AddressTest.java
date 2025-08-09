package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidAddressException;
import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidIdentifierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Nested
    @DisplayName("Valid address tests")
    class ValidAddresses {

        @Test
        @DisplayName("Should build address successfully with all fields and trim values")
        void shouldBuildSuccessfullyAndTrim() {

            Identifier id = new Identifier("123e4567-e89b-12d3-a456-426614174000");

            Address address = Address.builder()
                    .id(id)
                    .title("  Home  ")
                    .street("  ABC Street  ")
                    .city("  Istanbul  ")
                    .country("  TR  ")
                    .postalCode("  12345  ")
                    .makeDefault(true)
                    .build();

            assertEquals(id, address.id());
            assertEquals("Home", address.title());
            assertEquals("ABC Street", address.street());
            assertEquals("Istanbul", address.city());
            assertEquals("TR", address.country());
            assertEquals("12345", address.postalCode());
            assertTrue(address.isDefault());
        }

        @Test
        @DisplayName("Should set defaults when optional fields are null (id generated, title='New Address', postalCode='')")
        void shouldSetDefaultsForNulls() {
            Address address = Address.builder()
                    .id((Identifier) null)
                    .title(null)                // should become "New Address"
                    .street("ABC Street")
                    .city("Istanbul")
                    .country("TR")
                    .postalCode(null)      // should become ""
                    .makeDefault(false)
                    .build();

            assertNotNull(address.id());
            assertDoesNotThrow(() -> UUID.fromString(address.id().value()));
            assertEquals("New Address", address.title());
            assertEquals("", address.postalCode());
            assertFalse(address.isDefault());
        }

        @Test
        @DisplayName("Should accept id as String and as Identifier via builder")
        void shouldAcceptIdInDifferentForms() {

            String uuid = "123e4567-e89b-12d3-a456-426614174000";

            Address viaString = Address.builder()
                    .id(uuid)
                    .title("Work")
                    .street("XYZ Street")
                    .city("Istanbul")
                    .country("TR")
                    .build();

            Address viaIdentifier = Address.builder()
                    .id(new Identifier(uuid))
                    .title("Work")
                    .street("XYZ Street")
                    .city("Istanbul")
                    .country("TR")
                    .build();

            assertEquals(uuid, viaString.id().value());
            assertEquals(uuid, viaIdentifier.id().value());
        }

        @Test
        @DisplayName("Should trim title and allow empty postalCode (becomes empty string)")
        void shouldTrimTitleAndAllowEmptyPostalCode() {

            Address address = Address.builder()
                    .title("  Home #2  ")
                    .street("ABC Street")
                    .city("Istanbul")
                    .country("TR")
                    .postalCode("   ")
                    .build();

            assertEquals("Home #2", address.title());
            assertEquals("", address.postalCode());
        }

    }

    @Nested
    @DisplayName("Invalid address tests")
    class InvalidAddresses {

        @Test
        @DisplayName("Should throw when street is null or blank")
        void shouldThrowForInvalidStreet() {
            InvalidAddressException ex1 = assertThrows(
                    InvalidAddressException.class,
                    () -> Address.builder()
                            .street(null)
                            .city("Istanbul")
                            .country("TR")
                            .build()
            );
            assertEquals("Street cannot be null or empty", ex1.getMessage());

            InvalidAddressException ex2 = assertThrows(
                    InvalidAddressException.class,
                    () -> Address.builder()
                            .street("   ")
                            .city("Istanbul")
                            .country("TR")
                            .build()
            );
            assertEquals("Street cannot be null or empty", ex2.getMessage());
        }

        @Test
        @DisplayName("Should throw when city is null or blank")
        void shouldThrowForInvalidCity() {
            InvalidAddressException ex1 = assertThrows(
                    InvalidAddressException.class,
                    () -> Address.builder()
                            .street("ABC Street")
                            .city(null)
                            .country("TR")
                            .build()
            );
            assertEquals("City cannot be null or empty", ex1.getMessage());

            InvalidAddressException ex2 = assertThrows(
                    InvalidAddressException.class,
                    () -> Address.builder()
                            .street("ABC Street")
                            .city("   ")
                            .country("TR")
                            .build()
            );
            assertEquals("City cannot be null or empty", ex2.getMessage());
        }

        @Test
        @DisplayName("Should throw when country is null or blank")
        void shouldThrowForInvalidCountry() {
            InvalidAddressException ex1 = assertThrows(
                    InvalidAddressException.class,
                    () -> Address.builder()
                            .street("ABC Street")
                            .city("Istanbul")
                            .country(null)
                            .build()
            );
            assertEquals("Country cannot be null or empty", ex1.getMessage());

            InvalidAddressException ex2 = assertThrows(
                    InvalidAddressException.class,
                    () -> Address.builder()
                            .street("ABC Street")
                            .city("Istanbul")
                            .country("   ")
                            .build()
            );
            assertEquals("Country cannot be null or empty", ex2.getMessage());
        }

    }

    @Nested
    @DisplayName("Builder id parsing tests")
    class BuilderIdParsing {

        @Test
        @DisplayName("Should generate id when builder id is null")
        void shouldGenerateIdWhenNull() {
            Address address = Address.builder()
                    .id((String) null)
                    .street("ABC Street")
                    .city("Istanbul")
                    .country("TR")
                    .build();

            assertNotNull(address.id());
            assertDoesNotThrow(() -> UUID.fromString(address.id().value()));
        }

        @Test
        @DisplayName("Should throw InvalidIdentifierException when builder id string is not a UUID")
        void shouldThrowWhenIdStringInvalid() {
            assertThrows(
                    InvalidIdentifierException.class,
                    () -> Address.builder()
                            .id("not-a-uuid")
                            .street("ABC Street")
                            .city("Istanbul")
                            .country("TR")
                            .build()
            );
        }

    }

}
