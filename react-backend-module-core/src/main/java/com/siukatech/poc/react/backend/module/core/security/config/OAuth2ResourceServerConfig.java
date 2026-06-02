package com.siukatech.poc.react.backend.module.core.security.config;

import com.siukatech.poc.react.backend.module.core.security.oauth2.resource.OAuth2ResourceServerExtProp;
import com.siukatech.poc.react.backend.module.core.util.ResourceServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import java.text.ParseException;

@Slf4j
@Configuration
public class OAuth2ResourceServerConfig {
//    private final OAuth2ClientExtProp oAuth2ClientExtProp;
    private final OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp;

    public OAuth2ResourceServerConfig(
//            OAuth2ClientExtProp oAuth2ClientExtProp
//            ,
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp) {
//        this.oAuth2ClientExtProp = oAuth2ClientExtProp;
        this.oAuth2ResourceServerExtProp = oAuth2ResourceServerExtProp;
    }

//    @Bean
//    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
//        MyOpaqueTokenIntrospector myOpaqueTokenIntrospector
//                = new MyOpaqueTokenIntrospector(oAuth2ClientExtProp, oAuth2ResourceServerExtProp);
//        return myOpaqueTokenIntrospector;
//    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return (token -> {
            try {
                String tokenIssuer = ResourceServerUtil.getTokenIssuer(token);
////                String clientName = ResourceServerUtil.getClientName(oAuth2ClientExtProp, tokenIssuer);
////                OAuth2ClientExtProp.Registration registration = oAuth2ClientExtProp.getRegistration().get(clientName);
////                OAuth2ResourceServerExtProp.Jwt jwt = ResourceServerUtil.getResourceServerPropJwt(oAuth2ResourceServerExtProp, clientName);
//////                NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
//////                        .withIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri())
//////                        .jwsAlgorithm(SignatureAlgorithm.RS512)
//////                        .build();
////                log.debug("jwtDecode - tokenIssuer: [{}], clientName: [{}], jwt: [{}]", tokenIssuer, clientName, jwt);
                String issuerExt = ResourceServerUtil.getTokenIssuer(oAuth2ResourceServerExtProp, tokenIssuer);
                log.debug("jwtDecode - tokenIssuer: [{}], issuerExt: [{}]", tokenIssuer, issuerExt);
                assert issuerExt != null;
                NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerExt);
                OAuth2TokenValidator<Jwt> withIssuerJwtTokenValidator = JwtValidators.createDefaultWithIssuer(issuerExt);
                OAuth2TokenValidator<Jwt> jwtDelegatingOAuth2TokenValidator = new DelegatingOAuth2TokenValidator<>(withIssuerJwtTokenValidator);
                jwtDecoder.setJwtValidator(jwtDelegatingOAuth2TokenValidator);
                return jwtDecoder.decode(token);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
