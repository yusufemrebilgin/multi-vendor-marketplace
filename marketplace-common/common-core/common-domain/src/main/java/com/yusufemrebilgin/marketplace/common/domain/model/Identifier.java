package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidIdentifierException;

import java.util.UUID;

/**
 * Immutable value object for a UUID-based identifier. <p>
 * Throws {@link InvalidIdentifierException} if null, empty or not a valid UUID.
 */
public record Identifier(String value) {

    public Identifier {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidIdentifierException("Identifier cannot be null or empty");
        }

        try {
            UUID.fromString(value.trim());
        } catch (IllegalArgumentException ex) {
            throw new InvalidIdentifierException("Invalid UUID format: '" + value + "'");
        }

        value = value.trim();
    }

    public static Identifier generate() {
        return new Identifier(UUID.randomUUID().toString());
    }

}
