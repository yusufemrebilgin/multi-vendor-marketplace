package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception;

public class KeycloakGatewayException extends RuntimeException {
    public KeycloakGatewayException(String message) {
        super(message);
    }
}
