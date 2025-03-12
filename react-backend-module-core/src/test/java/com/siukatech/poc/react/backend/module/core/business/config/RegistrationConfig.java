package com.siukatech.poc.react.backend.module.core.business.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The OAuth2ClientProperties in Tests can be initiated but not the injected one.
 * As a result, we can initiate a version for testing and return this "for testing" during @Spy with doReturn().
 */
@Deprecated
@ConfigurationProperties("spring.security.oauth2.client.registration.keycloak")
public class RegistrationConfig {
    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String scope;
    private String redirectUri;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public void setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
