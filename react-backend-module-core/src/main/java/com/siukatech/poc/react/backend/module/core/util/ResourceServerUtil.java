package com.siukatech.poc.react.backend.module.core.util;

import com.nimbusds.jwt.SignedJWT;
import com.siukatech.poc.react.backend.module.core.security.oauth2.client.OAuth2ClientExtProp;
import com.siukatech.poc.react.backend.module.core.security.oauth2.resource.OAuth2ResourceServerExtProp;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.util.Map;
import java.util.NoSuchElementException;

@NoArgsConstructor
public class ResourceServerUtil {

    public static SignedJWT getSignedJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT;
    }

    public static String getTokenIssuer(String token) throws ParseException {
        SignedJWT signedJWT = getSignedJWT(token);
        return signedJWT.getJWTClaimsSet().getIssuer();
    }

    public static Map.Entry<String, OAuth2ClientExtProp.Provider> getProviderEntry(
            OAuth2ClientExtProp oAuth2ClientExtProp, String issuerExt) {
        Map.Entry<String, OAuth2ClientExtProp.Provider> providerEntry = oAuth2ClientExtProp
//                .getProvider()
                .getProviderExt()
                .entrySet().stream()
                .filter(entry -> (
                        issuerExt.equals(entry.getValue().getIssuerExt())
                        || issuerExt.equals(entry.getValue().getIssuerUri())
                ))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "OAuth2ClientExtProp.Provider not found, issuerExt: [%s]".formatted(issuerExt)))
                ;
        return providerEntry;
    }

    public static Map.Entry<String, OAuth2ResourceServerExtProp.Jwt> getJwtEntry(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerExt) {
        Map.Entry<String, OAuth2ResourceServerExtProp.Jwt> jwtEntry = oAuth2ResourceServerExtProp
                .getJwt().entrySet().stream()
                .filter(entry -> (
                        issuerExt.equals(entry.getValue().getIssuerExt())
                        || issuerExt.equals(entry.getValue().getIssuerUri())
                ))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "OAuth2ResourceServerExtProp.Jwt not found, issuerExt: [%s]".formatted(issuerExt)))
                ;
        return jwtEntry;
    }

    public static Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> getOpaqueTokenEntry(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerExt) {
        Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> opaqueTokenEntry = oAuth2ResourceServerExtProp
                .getOpaquetoken().entrySet().stream()
                .filter(entry -> (
                        issuerExt.equals(entry.getValue().getIssuerExt())
                ))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "OAuth2ResourceServerExtProp.Opaquetoken not found, issuerExt: [%s]".formatted(issuerExt)))
                ;
        return opaqueTokenEntry;
    }

    public static String getClientName(OAuth2ClientExtProp oAuth2ClientPropExt, String issuerExt) {
        Map.Entry<String, OAuth2ClientExtProp.Provider> providerEntry = getProviderEntry(oAuth2ClientPropExt, issuerExt);
        String clientName = providerEntry.getKey();
        return clientName;
    }

    public static String getClientName(OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerExt) {
        String clientName = null;
        if (oAuth2ResourceServerExtProp.getJwt() != null) {
            Map.Entry<String, OAuth2ResourceServerExtProp.Jwt> jwtEntry =
                    getJwtEntry(oAuth2ResourceServerExtProp, issuerExt);
            clientName = jwtEntry.getKey();
        }
        else if (oAuth2ResourceServerExtProp.getOpaquetoken() != null) {
            Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> opaquetokenEntry =
                    getOpaqueTokenEntry(oAuth2ResourceServerExtProp, issuerExt);
            clientName = opaquetokenEntry.getKey();
        }
        return clientName;
    }

    public static OAuth2ResourceServerExtProp.Jwt getResourceServerPropJwt(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String clientName) {
        OAuth2ResourceServerExtProp.Jwt jwt = oAuth2ResourceServerExtProp.getJwt().entrySet().stream()
                .filter(entry -> entry.getKey().equals(clientName))
                .map(entry -> entry.getValue())
                .findFirst()
                .orElse(null)
                ;
        return jwt;
    }

    public static String getTokenIssuer(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String issuerExt) {
        String issuerExtOut = null;
        if (oAuth2ResourceServerExtProp.getJwt() != null) {
            Map.Entry<String, OAuth2ResourceServerExtProp.Jwt> jwtEntry =
                    getJwtEntry(oAuth2ResourceServerExtProp, issuerExt);
            issuerExtOut = jwtEntry.getValue().getIssuerExt();
        }
        else if (oAuth2ResourceServerExtProp.getOpaquetoken() != null) {
            Map.Entry<String, OAuth2ResourceServerExtProp.Opaquetoken> opaquetokenEntry =
                    getOpaqueTokenEntry(oAuth2ResourceServerExtProp, issuerExt);
            issuerExtOut = opaquetokenEntry.getValue().getIssuerExt();
        }
        return issuerExtOut;
    }

}
