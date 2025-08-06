package com.yusufemrebilgin.marketplace.auth.domain.model;

import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidPasswordException;

import java.util.regex.Pattern;

public record Password(String raw) {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 32;
    private static final int TO_STRING_MASK_SIZE = 12;
    private static final Pattern COMPLEXITY_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).+$");

    public Password {
        if (raw == null || raw.trim().isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");
        if (raw.length() < MIN_LENGTH || raw.length() > MAX_LENGTH)
            throw new InvalidPasswordException("Password must be between %d and %d characters".formatted(MIN_LENGTH, MAX_LENGTH));
        if (!COMPLEXITY_PATTERN.matcher(raw).matches())
            throw new InvalidPasswordException("Password must contain both letters and digits");
    }

    @Override
    @SuppressWarnings("all")
    public String toString() {
        return "*".repeat(TO_STRING_MASK_SIZE);
    }

}
