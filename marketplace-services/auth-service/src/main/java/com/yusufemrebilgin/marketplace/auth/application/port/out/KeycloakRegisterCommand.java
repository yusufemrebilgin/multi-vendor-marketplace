package com.yusufemrebilgin.marketplace.auth.application.port.out;

import com.yusufemrebilgin.marketplace.auth.application.port.in.RegisterCommand;
import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.Role;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;
import com.yusufemrebilgin.marketplace.common.domain.model.Email;

public record KeycloakRegisterCommand(
        String firstName,
        String lastName,
        Username username,
        Password password,
        Email email,
        Role role
) {

    public static KeycloakRegisterCommand from(RegisterCommand cmd, Role role) {
        return new KeycloakRegisterCommand(
                cmd.firstName(),
                cmd.lastName(),
                cmd.username(),
                cmd.password(),
                cmd.email(),
                role
        );
    }

}
