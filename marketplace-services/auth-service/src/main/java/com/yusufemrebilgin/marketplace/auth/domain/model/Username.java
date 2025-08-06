package com.yusufemrebilgin.marketplace.auth.domain.model;

import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidUsernameException;

import java.util.regex.Pattern;

public record Username(String value) {

    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 20;

    // Allows: lowercase letters, digits, dots, underscores, hyphens
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9._-]+$");

    public Username {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        String normalized = value.trim().toLowerCase();
        if (normalized.length() < MIN_LENGTH || normalized.length() > MAX_LENGTH) {
            throw new InvalidUsernameException("Username must be between %d and %d characters".formatted(MIN_LENGTH, MAX_LENGTH));
        }
        if (!USERNAME_PATTERN.matcher(normalized).matches()) {
            throw new InvalidUsernameException("Username can only contain lowercase letters, digits, dots, underscores, and hyphens");
        }

        value = normalized;
    }

}
