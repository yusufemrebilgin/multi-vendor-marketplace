package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception;

public class KeycloakEmailAlreadyExistsException extends RuntimeException {
    public KeycloakEmailAlreadyExistsException() {
        super("User already exists with the same email");
    }
}
