package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception;

public class KeycloakRoleNotFoundException extends RuntimeException {
    public KeycloakRoleNotFoundException(String message) {
        super(message);
    }
}
