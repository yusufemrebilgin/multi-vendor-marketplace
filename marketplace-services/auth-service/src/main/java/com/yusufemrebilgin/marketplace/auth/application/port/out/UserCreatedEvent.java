package com.yusufemrebilgin.marketplace.auth.application.port.out;

public record UserCreatedEvent(
        String id,
        String firstName,
        String lastName,
        String email,
        String identityNumber
) {}
