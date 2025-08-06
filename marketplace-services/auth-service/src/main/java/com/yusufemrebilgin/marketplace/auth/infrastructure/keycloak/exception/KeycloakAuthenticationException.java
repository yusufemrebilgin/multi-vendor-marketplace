package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception;

public class KeycloakAuthenticationException extends RuntimeException {
    public KeycloakAuthenticationException(String message) {
        super(message);
    }
}
