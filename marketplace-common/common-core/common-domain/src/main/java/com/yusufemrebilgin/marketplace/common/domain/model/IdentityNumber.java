package com.yusufemrebilgin.marketplace.common.domain.model;

import com.yusufemrebilgin.marketplace.common.domain.exception.InvalidIdentityNumberException;

import java.util.regex.Pattern;

/**
 * Immutable value object for an 11-digit identity number. <p>
 * Throws {@link InvalidIdentityNumberException} if the identity number is invalid.
 */
public record IdentityNumber(String value) {

    private static final Pattern IDENTITY_NUMBER_PATTERN = Pattern.compile("^\\d{11}$");

    public IdentityNumber {
        if (value == null || !IDENTITY_NUMBER_PATTERN.matcher(value).matches()) {
            throw new InvalidIdentityNumberException("Identity number must be exactly 11 digits");
        }
    }

}
