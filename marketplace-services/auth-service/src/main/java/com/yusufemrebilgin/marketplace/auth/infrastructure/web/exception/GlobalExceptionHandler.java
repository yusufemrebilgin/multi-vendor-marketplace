package com.yusufemrebilgin.marketplace.auth.infrastructure.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidEmailException;
import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidPasswordException;
import com.yusufemrebilgin.marketplace.auth.domain.exception.InvalidUsernameException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakAuthenticationException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakEmailAlreadyExistsException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakGatewayException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakRoleNotFoundException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakUsernameAlreadyExistsException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakUserCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorResponse(String title, String detail, String path, String timestamp) {
        public static ErrorResponse of(Exception exception, HttpStatus status, String requestPath) {
            return new ErrorResponse(
                    status.getReasonPhrase(),
                    exception.getMessage(),
                    requestPath,
                    Instant.now().toString()
            );
        }
    }

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

    @ExceptionHandler({
            InvalidEmailException.class,
            InvalidPasswordException.class,
            InvalidUsernameException.class
    })
    public ResponseEntity<ErrorResponse> handleValidationException(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {

        logger.error(
                "Validation failed at endpoint: '{}' - Status: '{}' - Error: '{}'",
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                ex.getClass().getSimpleName(),
                ex
        );

        String timestamp = Instant.now().toString();

        List<ErrorResponse> errors = ex.getAllErrors().stream()
                .map(error -> new ErrorResponse(
                        String.format("Validation failed for field [%s]", ((FieldError) error).getField()),
                        error.getDefaultMessage(),
                        request.getDescription(false).replaceAll("uri=", ""),
                        timestamp
                )).toList();

        if (errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (errors.size() == 1) {
            return ResponseEntity.badRequest().body(errors.getFirst());
        }

        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception ex, WebRequest request) {
        return new ResponseEntity<>(ErrorResponse.of(
                ex,
                status,
                request.getDescription(false).replaceAll("uri=", "")),
                status
        );
    }

}
