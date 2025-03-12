package com.siukatech.poc.react.backend.module.core.security.resourceserver;

import com.siukatech.poc.react.backend.module.core.util.ResourceServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class MyOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp;
    private Map<String, OpaqueTokenIntrospector> opaqueTokenIntrospectorMap;

    public MyOpaqueTokenIntrospector(OAuth2ClientProperties oAuth2ClientProperties
            , OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.oAuth2ResourceServerExtProp = oAuth2ResourceServerExtProp;
        opaqueTokenIntrospectorMap = new HashMap<>();
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        try {
            String issuerUri = ResourceServerUtil.getIssuerUri(token);
            String clientName = oAuth2ClientProperties.getProvider()
                    .entrySet().stream()
                    .filter(entry -> {
                        boolean result = entry.getValue().getIssuerUri().equals(issuerUri);
                        log.debug("introspect - clientName.filter - "
                                + "result: [{}], issuerUri: [{}], entry.getValue.getIssuerUri: [{}]"
                                , result, issuerUri, entry.getValue().getIssuerUri());
                        return result;
                    })
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null)
                    ;
            if (clientName == null) {
                String errorMessage = "clientName is not found with issuerUri: [%s] in spring.security.oauth2.client.provider"
                        .formatted(issuerUri);
                throw new NoSuchElementException(errorMessage);
            }
            OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(clientName);
            OAuth2ResourceServerProperties.Opaquetoken opaqueToken = oAuth2ResourceServerExtProp.getOpaquetoken()
                    .entrySet().stream()
                    .filter(entry -> {
                        boolean result = entry.getKey().equals(clientName);
                        log.debug("introspect - opaqueToken.filter - "
                                        + "result: [{}], clientName: [{}], entry.getKey: [{}]"
                                , result, clientName, entry.getValue().getIssuerUri());
                        return result;
                    })
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null)
                    ;
            if (opaqueToken == null) {
                String errorMessage = "opaqueToken is not found with clientName: [%s] in spring.security.oauth2.resource-server.opaque-token"
                        .formatted(clientName);
                throw new NoSuchElementException(errorMessage);
            }
            OpaqueTokenIntrospector opaqueTokenIntrospector = opaqueTokenIntrospectorMap.get(clientName);
            if ( opaqueTokenIntrospector == null ) {
                opaqueTokenIntrospector = new SpringOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri()
                        , registration.getClientId(), registration.getClientSecret());
                opaqueTokenIntrospectorMap.put(clientName, opaqueTokenIntrospector);
            }
            log.debug("introspect - issuerUri: [{}], clientName: [{}], opaqueToken: [{}]"
                    , issuerUri, clientName, opaqueToken);
            return opaqueTokenIntrospector.introspect(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
