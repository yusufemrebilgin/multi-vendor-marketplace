package com.yusufemrebilgin.marketplace.auth.application.port.out;

public record UserCreatedEvent(
        String userId,
        String firstName,
        String lastName,
        String email,
        String identityNumber
) {}
