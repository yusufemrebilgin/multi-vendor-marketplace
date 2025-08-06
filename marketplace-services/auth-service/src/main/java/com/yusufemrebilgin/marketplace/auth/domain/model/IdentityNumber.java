package com.yusufemrebilgin.marketplace.auth.domain.model;

import java.util.regex.Pattern;

public record IdentityNumber(String value) {

    private static final Pattern IDENTITY_NUMBER_PATTERN = Pattern.compile("^\\d{11}$");

    public IdentityNumber {
        if (value == null || !IDENTITY_NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Identity number must be exactly 11 digits");
        }
    }

}
