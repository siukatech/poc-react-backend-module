package com.siukatech.poc.react.backend.module.core.util;

import com.nimbusds.jwt.SignedJWT;
import com.siukatech.poc.react.backend.module.core.security.resourceserver.OAuth2ResourceServerExtProp;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.text.ParseException;
import java.util.Map;

public class ResourceServerUtil {

    public static SignedJWT getSignedJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT;
    }

    public static String getIssuerUri(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getIssuer();
    }

    public static String getClientName(OAuth2ClientProperties oAuth2ClientProperties, String issuerUri) {
        String clientName = oAuth2ClientProperties.getProvider().entrySet().stream()
                .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                .map(entry -> entry.getKey())
                .findFirst()
                .orElse(null)
                ;
        return clientName;
    }

    public static String getClientName(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerUri) {
        String clientName = null;
        if (oAuth2ResourceServerExtProp.getJwt() != null) {
            clientName = oAuth2ResourceServerExtProp.getJwt().entrySet().stream()
                    .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                    .map(entry -> entry.getKey())
                    .findFirst()
                    .orElse(null)
                    ;
        }
        else if (oAuth2ResourceServerExtProp.getOpaquetoken() != null) {
            clientName = oAuth2ResourceServerExtProp.getOpaquetoken().entrySet().stream()
                    .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null)
            ;
        }
        return clientName;
    }

    public static OAuth2ResourceServerProperties.Jwt getResourceServerPropJwt(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String clientName) {
        OAuth2ResourceServerProperties.Jwt jwt = oAuth2ResourceServerExtProp.getJwt().entrySet().stream()
                .filter(entry -> entry.getKey().equals(clientName))
                .map(entry -> entry.getValue())
                .findFirst()
                .orElse(null)
                ;
        return jwt;
    }

    public static String getIssuerUri(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerUriSrc) {
        String issuerUri = null;
        if (oAuth2ResourceServerExtProp.getJwt() != null) {
            issuerUri = oAuth2ResourceServerExtProp.getJwt().entrySet().stream()
                    .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUriSrc))
                    .map(entry -> entry.getValue().getIssuerUri())
                    .findFirst()
                    .orElse(null)
            ;
        }
        else if (oAuth2ResourceServerExtProp.getOpaquetoken() != null) {
            issuerUri = oAuth2ResourceServerExtProp.getOpaquetoken().entrySet().stream()
                    .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUriSrc))
                    .map(entry -> entry.getValue().getIssuerUri())
                    .findFirst()
                    .orElse(null)
            ;
        }
        return issuerUri;
    }

}
