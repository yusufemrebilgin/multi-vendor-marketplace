package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidAddressException;

/**
 * Immutable value object for a postal address.
 * Requires non-empty street, city, and country. <p>
 * Throws {@link InvalidAddressException} if required fields are missing or invalid.
 */
public record Address(
        Identifier id,
        String title,
        String street,
        String city,
        String country,
        String postalCode,
        boolean isDefault
) {

    public Address {
        if (street == null || street.trim().isEmpty())
            throw new InvalidAddressException("Street cannot be null or empty");
        if (city == null || city.trim().isEmpty())
            throw new InvalidAddressException("City cannot be null or empty");
        if (country == null || country.trim().isEmpty())
            throw new InvalidAddressException("Country cannot be null or empty");

        id = (id == null) ? Identifier.generate() : id;
        title = (title == null) ? "New Address" : title.trim();
        street = street.trim();
        city = city.trim();
        country = country.trim();
        postalCode = (postalCode == null) ? "" : postalCode.trim();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Identifier id;
        private String title;
        private String street;
        private String city;
        private String country;
        private String postalCode;
        private boolean isDefault;

        private Builder() {}

        public Builder id(String id) {
            this.id = (id == null) ? Identifier.generate() : new Identifier(id);
            return this;
        }

        public Builder id(Identifier id) {
            this.id = (id == null) ? Identifier.generate() : id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder makeDefault(boolean makeDefault) {
            this.isDefault = makeDefault;
            return this;
        }

        /**
         * Builds the Address with validation.
         *
         * @return validated {@code Address} instance
         * @throws InvalidAddressException if required fields are missing or invalid
         */

        public Address build() {
            return new Address(id, title, street, city, country, postalCode, isDefault);
        }

    }

}
