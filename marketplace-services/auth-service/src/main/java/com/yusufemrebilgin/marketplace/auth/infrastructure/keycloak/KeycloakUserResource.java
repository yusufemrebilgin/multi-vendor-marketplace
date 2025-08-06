package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak;

import com.yusufemrebilgin.marketplace.auth.application.port.out.KeycloakRegisterCommand;

import java.util.List;

public record KeycloakUserResource(
        String username,
        String firstName,
        String lastName,
        String email,
        List<Credential> credentials,
        boolean enabled,
        boolean emailVerified
) {

    private static final String PASSWORD_CREDENTIAL_TYPE = "password";

    public record Credential(
            String type, String value, boolean temporary
    ) {}

    public static KeycloakUserResource from(KeycloakRegisterCommand cmd) {
        return new KeycloakUserResource(
                cmd.username().value(),
                cmd.firstName(),
                cmd.lastName(),
                cmd.email().value(),
                List.of(new Credential(PASSWORD_CREDENTIAL_TYPE, cmd.password().raw(), false)),
                true,
                false
        );
    }

}
