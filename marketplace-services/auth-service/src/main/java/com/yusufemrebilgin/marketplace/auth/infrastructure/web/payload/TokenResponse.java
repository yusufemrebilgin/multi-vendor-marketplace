package com.yusufemrebilgin.marketplace.auth.infrastructure.web.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yusufemrebilgin.marketplace.auth.domain.model.TokenPair;

public record TokenResponse(

        @JsonProperty("accessToken")
        String accessToken,

        @JsonProperty("refreshToken")
        String refreshToken,

        @JsonProperty("expiresInMs")
        long expiresInMs

) {

    public static TokenResponse from(TokenPair tokenPair) {
        return new TokenResponse(
                tokenPair.accessToken(),
                tokenPair.refreshToken(),
                tokenPair.expiresInMs()
        );
    }

}
