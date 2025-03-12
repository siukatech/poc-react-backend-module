package com.siukatech.poc.react.backend.module.core.security;

import com.siukatech.poc.react.backend.module.core.security.config.AuthorizationDataProviderConfig;
import com.siukatech.poc.react.backend.module.core.security.config.OAuth2ResourceServerConfig;
import com.siukatech.poc.react.backend.module.core.security.config.Oauth2ClientRestTemplateConfig;
import com.siukatech.poc.react.backend.module.core.security.config.WebSecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan({"com.siukatech.poc.react.backend.core.security"})
@Import({
        Oauth2ClientRestTemplateConfig.class
        , AuthorizationDataProviderConfig.class
        , OAuth2ResourceServerConfig.class
//        , AuthenticationEntryPointConfig.class
        , WebSecurityConfig.class
})
//@Import({
//        ExternalizedJwtAuthenticationConverter.class
//        , KeycloakLogoutHandler.class
//        , SecurityConfig.class
//})
public class SecurityConfigImport {

}
