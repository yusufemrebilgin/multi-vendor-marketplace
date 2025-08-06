package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception;

public class KeycloakUserCreationException extends RuntimeException {

    public KeycloakUserCreationException(String message) {
        super(message);
    }

    public KeycloakUserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
