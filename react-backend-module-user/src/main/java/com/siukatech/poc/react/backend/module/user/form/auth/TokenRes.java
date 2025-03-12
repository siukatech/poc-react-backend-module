package com.siukatech.poc.react.backend.module.user.form.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRes(@JsonProperty("access_token") String accessToken
        , @JsonProperty("refresh_token") String refreshToken
        , @JsonProperty("expires_in") String expiresIn
        , @JsonProperty("refresh_expires_in") String refreshExpiresIn
        , @JsonProperty("token_type") String tokenType
        , @JsonProperty("not-before-policy") Integer notBeforePolicy
        , @JsonProperty("session_state") String sessionState
        , @JsonProperty("scope") String scope
//        , @JsonProperty("public_key") String publicKey
) {
}
