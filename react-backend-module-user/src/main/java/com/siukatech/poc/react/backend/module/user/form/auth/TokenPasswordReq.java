package com.siukatech.poc.react.backend.module.user.form.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPasswordReq implements TokenReq {
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("grant_type")
    private String grantType;
//    @JsonProperty("redirect_uri")
//    private String redirectUri;
    private String username;
    private String password;
}
