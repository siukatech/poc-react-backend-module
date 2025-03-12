package com.siukatech.poc.react.backend.module.user.form.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AuthCodeReq implements AuthReq {
    @JsonProperty("response_type")
    private String responseType;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("code_challenge")
    private String codeChallenge;
    @JsonProperty("code_challenge_method")
    private String codeChallengeMethod;
}
