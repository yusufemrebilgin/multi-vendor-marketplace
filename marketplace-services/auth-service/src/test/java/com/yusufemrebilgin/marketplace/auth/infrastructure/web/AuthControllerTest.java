package com.yusufemrebilgin.marketplace.auth.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusufemrebilgin.marketplace.auth.application.service.AuthService;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakAuthenticationException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakEmailAlreadyExistsException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakUsernameAlreadyExistsException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload.LoginRequest;
import com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload.RegistrationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;

    @Test
    @DisplayName("Should return token on successful login")
    void shouldLoginSuccessfully() throws Exception {

        var request = new LoginRequest("test-user", "test-s3cr3t");
        var tokenResponse = new TokenPair("access-token", "refresh-token", 900_000L);

        when(authService.login(any())).thenReturn(tokenResponse);

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.expiresInMs").value(900_000L));
    }

    @Test
    @DisplayName("Should return 401 when user credentials is invalid")
    void shouldReturn401WhenUserCredentialsIsInvalid() throws Exception {

        var request = new LoginRequest("test-user", "test-s3cr3t");

        doThrow(new KeycloakAuthenticationException("")).when(authService).login(any());

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 201 on successful registration")
    void shouldReturn201WhenRegistrationSuccessfully() throws Exception {

        var request = new RegistrationRequest(
                "Test",
                "User",
                "test-user",
                "test-s3cr3t",
                "testuser@example.com",
                "12345678901"
        );

        doNothing().when(authService).register(any());

        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 400 when validation fails")
    void shouldReturn400WhenValidationFails() throws Exception {

        var request = new RegistrationRequest(
                "", "", "", "", "", ""
        );

        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when email already exists")
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        var request = new RegistrationRequest(
                "Test",
                "User",
                "test-user",
                "test-s3cr3t",
                "testuser@example.com",
                "12345678901"
        );

        doThrow(new KeycloakEmailAlreadyExistsException()).when(authService).register(any());

        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 409 when username already exists")
    void shouldReturn409WhenUsernameAlreadyExists() throws Exception {

        var request = new RegistrationRequest(
                "Test",
                "User",
                "test-user",
                "test-s3cr3t",
                "testuser@example.com",
                "12345678901"
        );

        doThrow(new KeycloakUsernameAlreadyExistsException()).when(authService).register(any());

        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

}