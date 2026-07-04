package com.siukatech.poc.react.backend.module.core.security;

import com.siukatech.poc.react.backend.module.core.security.config.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

//@ComponentScan(value = {"com.siukatech.poc.react.backend.module.core.security"})
@ComponentScan(basePackageClasses = {SecurityConfigImport.class})
@Import({
        Oauth2ClientRestTemplateConfig.class
        , AuthorizationDataProviderConfig.class
        , OAuth2ResourceServerConfig.class
//        , AuthenticationEntryPointConfig.class
        , PermissionControlConfig.class
        , WebSecurityConfig.class
})
//@Import({
//        ExternalizedJwtAuthenticationConverter.class
//        , KeycloakLogoutHandler.class
//        , SecurityConfig.class
//})
public class SecurityConfigImport {

}
