package com.yusufemrebilgin.marketplace.auth.infrastructure.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;

public record KeycloakTokenResource(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("expires_in")
        long expiresIn

) {

    public TokenPair toDomain() {
        return new TokenPair(accessToken, refreshToken, expiresIn);
    }

}
