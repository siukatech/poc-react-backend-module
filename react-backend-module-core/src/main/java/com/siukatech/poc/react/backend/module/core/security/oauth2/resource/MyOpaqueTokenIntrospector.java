package com.siukatech.poc.react.backend.module.core.security.oauth2.resource;

import com.siukatech.poc.react.backend.module.core.security.oauth2.client.OAuth2ClientExtProp;
import com.siukatech.poc.react.backend.module.core.util.ResourceServerUtil;
import lombok.extern.slf4j.Slf4j;
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

    private final OAuth2ClientExtProp oAuth2ClientExtProp;
    private final OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp;
    private final Map<String, OpaqueTokenIntrospector> opaqueTokenIntrospectorMap;

    public MyOpaqueTokenIntrospector(OAuth2ClientExtProp oAuth2ClientExtProp
            , OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp) {
        this.oAuth2ClientExtProp = oAuth2ClientExtProp;
        this.oAuth2ResourceServerExtProp = oAuth2ResourceServerExtProp;
        this.opaqueTokenIntrospectorMap = new HashMap<>();
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        try {
            String tokenIssuer = ResourceServerUtil.getTokenIssuer(token);
            String clientName = oAuth2ClientExtProp
//                    .getProvider()
                    .getProviderExt()
                    .entrySet().stream()
                    .filter(entry -> {
                        OAuth2ClientExtProp.Provider provider = entry.getValue();
                        boolean result = (tokenIssuer.equals(provider.getIssuerUri())
                                || tokenIssuer.equals(provider.getIssuerExt()));
                        log.debug("introspect - clientName.filter - "
                                + "result: [{}], tokenIssuer: [{}], provider.getIssuerUri: [{}], provider.getIssuerExt: [{}]"
                                , result, tokenIssuer, provider.getIssuerUri(), provider.getIssuerExt());
                        return result;
                    })
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null)
                    ;
            if (clientName == null) {
                String errorMessage = "clientName is not found with issuerUri: [%s] in spring.security.oauth2.client.provider"
                        .formatted(tokenIssuer);
                throw new NoSuchElementException(errorMessage);
            }
            OAuth2ClientExtProp.Registration registration = oAuth2ClientExtProp.getRegistration().get(clientName);
            OAuth2ResourceServerExtProp.Opaquetoken opaqueToken = oAuth2ResourceServerExtProp.getOpaquetoken()
                    .entrySet().stream()
                    .filter(entry -> {
                        OAuth2ResourceServerExtProp.Opaquetoken opaquetoken = entry.getValue();
                        boolean result = entry.getKey().equals(clientName);
                        log.debug("introspect - opaqueToken.filter - "
                                        + "result: [{}], clientName: [{}], entry.getKey: [{}], opaquetoken.getIssuerExt: [{}]"
                                , result, clientName, entry.getKey(), opaquetoken.getIssuerExt());
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
                    , tokenIssuer, clientName, opaqueToken);
            return opaqueTokenIntrospector.introspect(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
