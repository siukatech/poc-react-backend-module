package com.siukatech.poc.react.backend.module.core.util;

import com.nimbusds.jwt.SignedJWT;
import com.siukatech.poc.react.backend.module.core.security.resourceserver.OAuth2ResourceServerExtProp;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.text.ParseException;
import java.util.Map;
import java.util.NoSuchElementException;

public class ResourceServerUtil {

    public static SignedJWT getSignedJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT;
    }

    public static String getIssuerUri(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getIssuer();
    }

    public static Map.Entry<String, OAuth2ClientProperties.Provider> getProviderEntry(
            OAuth2ClientProperties oAuth2ClientProperties, String issuerUri) {
        Map.Entry<String, OAuth2ClientProperties.Provider> providerEntry = oAuth2ClientProperties
                .getProvider().entrySet().stream()
                .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "OAuth2ClientProperties.Provider not found, issuerUri: [%s]".formatted(issuerUri)))
                ;
        return providerEntry;
    }

    public static Map.Entry<String, OAuth2ResourceServerProperties.Jwt> getJwtEntry(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerUri) {
        Map.Entry<String, OAuth2ResourceServerProperties.Jwt> jwtEntry = oAuth2ResourceServerExtProp
                .getJwt().entrySet().stream()
                .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "OAuth2ResourceServerProperties.Jwt not found, issuerUri: [%s]".formatted(issuerUri)))
                ;
        return jwtEntry;
    }

    public static Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> getOpaqueTokenEntry(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerUri) {
        Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> opaqueTokenEntry = oAuth2ResourceServerExtProp
                .getOpaquetoken().entrySet().stream()
                .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "OAuth2ResourceServerExtProp.Opaquetoken not found, issuerUri: [%s]".formatted(issuerUri)))
                ;
        return opaqueTokenEntry;
    }

    public static String getClientName(OAuth2ClientProperties oAuth2ClientProperties, String issuerUri) {
        Map.Entry<String, OAuth2ClientProperties.Provider> providerEntry = getProviderEntry(oAuth2ClientProperties, issuerUri);
        String clientName = providerEntry.getKey();
        return clientName;
    }

    public static String getClientName(OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerUri) {
        String clientName = null;
        if (oAuth2ResourceServerExtProp.getJwt() != null) {
            Map.Entry<String, OAuth2ResourceServerProperties.Jwt> jwtEntry =
                    getJwtEntry(oAuth2ResourceServerExtProp, issuerUri);
            clientName = jwtEntry.getKey();
        }
        else if (oAuth2ResourceServerExtProp.getOpaquetoken() != null) {
            Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> opaquetokenEntry =
                    getOpaqueTokenEntry(oAuth2ResourceServerExtProp, issuerUri);
            clientName = opaquetokenEntry.getKey();
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
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerUri) {
        String issuerUriOut = null;
        if (oAuth2ResourceServerExtProp.getJwt() != null) {
            Map.Entry<String, OAuth2ResourceServerProperties.Jwt> jwtEntry =
                    getJwtEntry(oAuth2ResourceServerExtProp, issuerUri);
            issuerUriOut = jwtEntry.getValue().getIssuerUri();
        }
        else if (oAuth2ResourceServerExtProp.getOpaquetoken() != null) {
            Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> opaquetokenEntry =
                    getOpaqueTokenEntry(oAuth2ResourceServerExtProp, issuerUri);
            issuerUriOut = opaquetokenEntry.getValue().getIssuerUri();
        }
        return issuerUriOut;
    }

}
