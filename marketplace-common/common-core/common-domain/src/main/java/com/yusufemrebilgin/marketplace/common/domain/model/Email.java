package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidEmailException;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Immutable value object for a validated email address.
 * <p>
 * Throws:
 * <ul>
 *   <li>{@link IllegalArgumentException} if null or empty</li>
 *   <li>{@link InvalidEmailException} if the format is invalid</li>
 * </ul>
 */
public record Email(String value) {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$", Pattern.CASE_INSENSITIVE);

    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be null or empty");
        }

        String normalized = value.trim().toLowerCase(Locale.ENGLISH);
        if (!EMAIL_REGEX.matcher(normalized).matches()) {
            throw new InvalidEmailException("Invalid email: '" + value + "'");
        }

        value = normalized;
    }

}
