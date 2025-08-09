package com.yusufemrebilgin.marketplace.auth.domain.model;

public record TokenPair(String accessToken, String refreshToken, long expiresInMs) {

    public TokenPair {
        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Access token cannot be null or empty");
        }
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Refresh token cannot be null or empty");
        }
        if (expiresInMs <= 0) {
            throw new IllegalArgumentException("Expiration must be positive");
        }

        accessToken = accessToken.trim();
        refreshToken = refreshToken.trim();
    }


}
