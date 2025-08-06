package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak;

import com.yusufemrebilgin.marketplace.auth.application.port.out.KeycloakClient;
import com.yusufemrebilgin.marketplace.auth.application.port.out.KeycloakRegisterCommand;
import com.yusufemrebilgin.marketplace.auth.domain.model.Password;
import com.yusufemrebilgin.marketplace.auth.domain.model.Role;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;
import com.yusufemrebilgin.marketplace.auth.domain.model.Username;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakAuthenticationException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakGatewayException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakEmailAlreadyExistsException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakRoleNotFoundException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakUsernameAlreadyExistsException;
import com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak.exception.KeycloakUserCreationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class KeycloakClientImpl implements KeycloakClient {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakClientImpl.class);

    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);

    private final WebClient keycloakWebClient;
    private final KeycloakProperties keycloakProperties;

    @Override
    public TokenPair login(Username username, Password password) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", GRANT_TYPE_PASSWORD);
        form.add("client_id", keycloakProperties.clientId());
        form.add("client_secret", keycloakProperties.clientSecret());
        form.add("username", username.value());
        form.add("password", password.raw());

        return keycloakWebClient
                .post()
                .uri("/realms/{realm}/protocol/openid-connect/token", keycloakProperties.realm())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> handleError(response, "User Login"))
                .bodyToMono(KeycloakTokenResource.class)
                .timeout(REQUEST_TIMEOUT)
                .blockOptional()
                .map(KeycloakTokenResource::toDomain)
                .orElseThrow(() -> new KeycloakAuthenticationException("Missing token in Keycloak login response"));
    }

    @Override
    public String register(KeycloakRegisterCommand command) {

        KeycloakUserResource user = KeycloakUserResource.from(command);
        String adminAuthToken = obtainAdminTokenFromKeycloak();

        String createdUserId = keycloakWebClient
                .post()
                .uri("/admin/realms/{realm}/users", keycloakProperties.realm())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAuthToken)
                .bodyValue(user)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> handleError(response, "User Registration"))
                .toBodilessEntity()
                .blockOptional()
                .map(response -> response.getHeaders().getLocation())
                .map(this::extractUserIdFromLocation)
                .orElseThrow(() -> new KeycloakUserCreationException("User creation failed: No location header"));

        logger.info("Keycloak user created with id '{}'", createdUserId);

        try {
            // Assign the default role (e.g., 'customer') immediately after account creation
            // to ensure the user has the minimum required access permissions from the start.
            assignDefaultRole(adminAuthToken, createdUserId);
            return createdUserId;
        } catch (KeycloakRoleNotFoundException ex) {
            logger.error("User registration failed: {}", ex.getMessage(), ex);
            performRollback(adminAuthToken, createdUserId);
            throw new KeycloakUserCreationException("User registration failed and rolled back: " + ex.getMessage(), ex);
        }
    }


    private String obtainAdminTokenFromKeycloak() {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS);
        form.add("client_id", keycloakProperties.adminClientId());
        form.add("client_secret", keycloakProperties.adminClientSecret());

        var retrySpec = Retry
                .backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(1))
                .filter(this::isRetryableException)
                .doBeforeRetry(retrySignal -> logger.debug(
                        "[Retry] Attempt {} due to: {}",
                        retrySignal.totalRetries() + 1,
                        retrySignal.failure().toString()
                ));

        return keycloakWebClient
                .post()
                .uri("/realms/{realm}/protocol/openid-connect/token", keycloakProperties.realm())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(KeycloakTokenResource.class)
                .timeout(REQUEST_TIMEOUT)
                .retryWhen(retrySpec)
                .blockOptional()
                .map(KeycloakTokenResource::accessToken)
                .orElseThrow(() -> new KeycloakAuthenticationException("Failed to obtain admin token"));
    }

    private void assignDefaultRole(String adminToken, String userId) {

        List<KeycloakRoleResource> existingRoles = keycloakWebClient
                .get()
                .uri("/admin/realms/{realm}/roles", keycloakProperties.realm())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .retrieve()
                .bodyToFlux(KeycloakRoleResource.class)
                .collectList()
                .blockOptional()
                .orElseThrow(() -> new KeycloakRoleNotFoundException("Unable to fetch roles"));

        KeycloakRoleResource defaultRole = existingRoles.stream()
                .filter(role -> role.roleName().equalsIgnoreCase(Role.defaultRole().name()))
                .findFirst()
                .orElseThrow(() -> new KeycloakRoleNotFoundException("Default role '" + Role.defaultRole() + "' not found"));

        keycloakWebClient
                .post()
                .uri("/admin/realms/{realm}/users/{userId}/role-mappings/realm", keycloakProperties.realm(), userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(defaultRole))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> handleError(response, "Assign Default Role"))
                .toBodilessEntity()
                .block();

        logger.info("Default role '{}' assigned to user '{}'", defaultRole.roleName(), userId);
    }

    private void deleteKeycloakUser(String adminToken, String userId) {
        keycloakWebClient
                .delete()
                .uri("/admin/realms/{realm}/users/{userId}", keycloakProperties.realm(), userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> handleError(response, "Rollback (delete user)"))
                .toBodilessEntity()
                .timeout(REQUEST_TIMEOUT)
                .block();
    }

    private void performRollback(String adminToken, String userId) {
        try {
            deleteKeycloakUser(adminToken, userId);
            logger.info("Successfully rolled back user creation for user '{}'", userId);
        } catch (Exception ex) {
            logger.error("Rollback failed for user '{}': {}", userId, ex.getMessage(), ex);
        }
    }

    private Mono<? extends Throwable> handleError(ClientResponse response, String context) {
        return response.bodyToMono(KeycloakErrorResponse.class).map(error -> {
            var status = (HttpStatus) response.statusCode();
            logger.error("[Keycloak Error] {} - Status {} - Body: {}", context, status, error.errorMessage);

            return switch (status) {
                case UNAUTHORIZED ->
                        new KeycloakAuthenticationException("Invalid username or password");

                case CONFLICT -> {
                    if (error.isEmailConflict()) yield new KeycloakEmailAlreadyExistsException();
                    if (error.isUsernameConflict()) yield new KeycloakUsernameAlreadyExistsException();
                    yield new KeycloakGatewayException("Conflict: " + error.errorMessage);
                }

                default -> {
                    if (status.is4xxClientError()) yield new KeycloakGatewayException("Keycloak client error: " + error.errorMessage);
                    if (status.is5xxServerError()) yield new KeycloakGatewayException("Keycloak server error: " + error.errorMessage);
                    yield new KeycloakGatewayException("Unknown error from Keycloak: " + error.errorMessage);
                }
            };
        });
    }

    private boolean isRetryableException(Throwable throwable) {
        return throwable instanceof WebClientResponseException ex
                && (ex.getStatusCode().is5xxServerError() ||
                ex.getStatusCode() == HttpStatus.REQUEST_TIMEOUT);
    }

    private String extractUserIdFromLocation(URI location) {
        String locationStr = location.toString();
        return locationStr.substring(locationStr.lastIndexOf('/') + 1);
    }

    private record KeycloakErrorResponse(String errorMessage) {

        private static final String ERROR_MSG_EMAIL_CONFLICT = "User exists with same email";
        private static final String ERROR_MSG_USERNAME_CONFLICT = "User exists with same username";

        public boolean isEmailConflict() {
            return ERROR_MSG_EMAIL_CONFLICT.equalsIgnoreCase(errorMessage);
        }

        public boolean isUsernameConflict() {
            return ERROR_MSG_USERNAME_CONFLICT.equalsIgnoreCase(errorMessage);
        }

    }

}
