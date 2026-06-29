package com.siukatech.poc.react.backend.module.core.security;

import com.siukatech.poc.react.backend.module.core.security.config.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan({"com.siukatech.poc.react.backend.module.core.security"})
@Import({
        Oauth2ClientRestTemplateConfig.class
        , AuthorizationDataProviderConfig.class
        , OAuth2ResourceServerConfig.class
//        , AuthenticationEntryPointConfig.class
        , PermissionControlEvaluatorConfig.class
        , WebSecurityConfig.class
})
//@Import({
//        ExternalizedJwtAuthenticationConverter.class
//        , KeycloakLogoutHandler.class
//        , SecurityConfig.class
//})
public class SecurityConfigImport {

}
