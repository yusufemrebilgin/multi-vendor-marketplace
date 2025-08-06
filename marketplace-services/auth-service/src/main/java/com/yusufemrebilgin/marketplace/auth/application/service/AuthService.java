package com.yusufemrebilgin.marketplace.auth.application.service;

import com.yusufemrebilgin.marketplace.auth.application.port.in.LoginCommand;
import com.yusufemrebilgin.marketplace.auth.application.port.in.LoginUseCase;
import com.yusufemrebilgin.marketplace.auth.application.port.in.RegisterCommand;
import com.yusufemrebilgin.marketplace.auth.application.port.in.RegisterUseCase;
import com.yusufemrebilgin.marketplace.auth.application.port.out.KeycloakClient;
import com.yusufemrebilgin.marketplace.auth.application.port.out.KeycloakRegisterCommand;
import com.yusufemrebilgin.marketplace.auth.application.port.out.UserCreatedEvent;
import com.yusufemrebilgin.marketplace.auth.application.port.out.UserEventPublisher;
import com.yusufemrebilgin.marketplace.auth.application.annotation.UseCase;
import com.yusufemrebilgin.marketplace.auth.domain.model.Role;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class AuthService implements LoginUseCase, RegisterUseCase {

    private final KeycloakClient keycloakClient;
    private final UserEventPublisher userEventPublisher;

    @Override
    public TokenPair login(LoginCommand command) {
        return keycloakClient.login(command.username(), command.password());
    }

    @Override
    public void register(RegisterCommand command) {

        var cmd = KeycloakRegisterCommand.from(command, Role.defaultRole());
        String userId = keycloakClient.register(cmd);

        UserCreatedEvent event = new UserCreatedEvent(
                userId,
                command.firstName(),
                command.lastName(),
                command.email().value(),
                command.identityNumber().value()
        );

        userEventPublisher.publish(event);
    }

}
