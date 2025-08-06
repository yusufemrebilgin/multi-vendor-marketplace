package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakRoleResource(

        @JsonProperty("id")
        String userId,

        @JsonProperty("name")
        String roleName

) {}
