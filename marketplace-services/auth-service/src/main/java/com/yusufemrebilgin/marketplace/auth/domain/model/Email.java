package com.yusufemrebilgin.marketplace.auth.domain.model;

import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidEmailException;

import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$", Pattern.CASE_INSENSITIVE);

    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String normalized = value.trim().toLowerCase(Locale.ENGLISH);
        if (!EMAIL_REGEX.matcher(normalized).matches()) {
            throw new InvalidEmailException("Invalid email: '" + value + "'");
        }

        value = normalized;
    }

}
