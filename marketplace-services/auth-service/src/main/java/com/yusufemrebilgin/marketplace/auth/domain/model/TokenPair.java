package com.yusufemrebilgin.marketplace.auth.domain.model;

public record TokenPair(String accessToken, String refreshToken, long expiresInMs) {}
