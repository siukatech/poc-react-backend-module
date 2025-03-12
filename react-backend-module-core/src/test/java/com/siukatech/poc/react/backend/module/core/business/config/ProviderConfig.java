package com.siukatech.poc.react.backend.module.core.business.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The OAuth2ClientProperties in Tests can be initiated but not the injected one.
 * As a result, we can initiate a version for testing and return this "for testing" during @Spy with doReturn().
 */
@Deprecated
@ConfigurationProperties("spring.security.oauth2.client.provider.keycloak")
public class ProviderConfig {
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String issuerUri;
    private String jwkSetUri;
    private String userNameAttribute;

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public String getUserNameAttribute() {
        return userNameAttribute;
    }

    public void setUserNameAttribute(String userNameAttribute) {
        this.userNameAttribute = userNameAttribute;
    }
}
