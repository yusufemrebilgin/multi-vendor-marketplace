package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception;

public class KeycloakUsernameAlreadyExistsException extends RuntimeException {
    public KeycloakUsernameAlreadyExistsException() {
        super("User already exists with the same username");
    }
}
