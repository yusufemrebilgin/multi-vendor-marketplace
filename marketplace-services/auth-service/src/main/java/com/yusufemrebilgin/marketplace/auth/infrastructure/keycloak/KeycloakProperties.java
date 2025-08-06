package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(

        @NotBlank(message = "Keycloak url must not be blank")
        String url,

        @NotBlank(message = "Keycloak realm must not be blank")
        String realm,

        @NotBlank(message = "Client id must not be blank")
        String clientId,

        @NotBlank(message = "Client secret must not be blank")
        String clientSecret,

        @NotBlank(message = "Admin client id must not be blank")
        String adminClientId,

        @NotBlank(message = "Admin client secret must not be blank")
        String adminClientSecret,

        @NotBlank(message = "Service client id must not be blank")
        String serviceClientId,

        @NotBlank(message = "Service client secret must not be blank")
        String serviceClientSecret

) {}
