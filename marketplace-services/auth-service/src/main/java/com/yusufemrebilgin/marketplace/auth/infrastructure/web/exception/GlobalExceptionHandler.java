package com.yusufemrebilgin.marketplace.auth.infrastructure.web.exception;

import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakAuthenticationException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakEmailAlreadyExistsException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakGatewayException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakRoleNotFoundException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakUserCreationException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakUsernameAlreadyExistsException;
import com.yusufemrebilgin.marketplace.common.web.exception.BaseExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(KeycloakAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakAuthenticationEx(KeycloakAuthenticationException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(KeycloakEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakEmailAlreadyExistsEx(KeycloakEmailAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(KeycloakGatewayException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakGatewayExceptionEx(KeycloakGatewayException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, ex, request);
    }

    @ExceptionHandler(KeycloakRoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakRoleNotFoundEx(KeycloakRoleNotFoundException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(KeycloakUsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> KeycloakUserAlreadyExistsEx(KeycloakUsernameAlreadyExistsException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(KeycloakUserCreationException.class)
    public ResponseEntity<ErrorResponse> KeycloakUserCreationEx(KeycloakUserCreationException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, ex, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex, request);
    }

}
