package com.yusufemrebilgin.marketplace.auth.application.service;

import com.yusufemrebilgin.marketplace.auth.application.port.in.LoginCommand;
import com.yusufemrebilgin.marketplace.auth.application.port.in.RegisterCommand;
import com.yusufemrebilgin.marketplace.auth.application.port.out.KeycloakClient;
import com.yusufemrebilgin.marketplace.auth.application.port.out.UserEventPublisher;
import com.yusufemrebilgin.marketplace.auth.domain.model.Email;
import com.yusufemrebilgin.marketplace.auth.domain.model.IdentityNumber;
import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    KeycloakClient keycloakClient;

    @Mock
    UserEventPublisher userEventPublisher;

    @Test
    @DisplayName("Should login using KeycloakClient and return TokenPair")
    void shouldLoginSuccessfully() {
        // given
        var cmd = new LoginCommand(
                new Username("test-user"),
                new Password("test-s3cr3t")
        );

        TokenPair expected = new TokenPair(
                "access-token",
                "refresh-token",
                900_000L
        );

        when(keycloakClient.login(any(), any())).thenReturn(expected);

        // when
        TokenPair actual = authService.login(cmd);

        // then
        assertEquals(expected, actual);
        verify(keycloakClient).login(any(Username.class), any(Password.class));
        verifyNoInteractions(userEventPublisher);
    }

    @Test
    @DisplayName("Should register user and publish UserCreatedEvent")
    void shouldRegisterSuccessfully() {
        // given
        var cmd = new RegisterCommand(
                "Test",
                "User",
                new Username("test-user"),
                new Password("test-s3cr3t"),
                new Email("testuser@example.com"),
                new IdentityNumber("12345678901")
        );

        String generatedUserId = "generated-user-id";

        when(keycloakClient.register(any())).thenReturn(generatedUserId);

        // when
        authService.register(cmd);

        // then
        verify(keycloakClient).register(any());
        verify(userEventPublisher).publish(argThat(event ->
                event.userId().equals(generatedUserId) &&
                        event.firstName().equals("Test") &&
                        event.lastName().equals("User") &&
                        event.email().equals("testuser@example.com") &&
                        event.identityNumber().equals("12345678901")
        ));
    }


}