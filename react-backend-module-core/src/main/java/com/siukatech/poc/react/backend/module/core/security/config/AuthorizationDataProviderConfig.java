package com.siukatech.poc.react.backend.module.core.security.config;

import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.security.provider.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * The following beans are dependencies of beans in {@link WebSecurityConfig}.
 * <ul>
 *   <li>oauth2ClientRestTemplate</li>
 *   <li>remoteAuthorizationDataProvider</li>
 *   <li>databaseAuthorizationDataProvider</li>
 * </ul>
 *
 * As a result, this class has been introduced to handle that and prevent circular reference.
 *
 */
@Slf4j
@Configuration
public class AuthorizationDataProviderConfig {

//    private final AppCoreProp appCoreProp;
//    private final UserService userService;
//    private final RestTemplate oauth2ClientRestTemplate;

    public AuthorizationDataProviderConfig(
//            AppCoreProp appCoreProp
//            , UserService userService
//            , RestTemplate oauth2ClientRestTemplate
    ) {
//        this.appCoreProp = appCoreProp;
//        this.userService = userService;
//        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
        log.debug("constructor");
    }
//
//    @Bean("authorizationDataProvider")
//    @ConditionalOnProperty("app.api.my-user-info")
//    public AuthorizationDataProvider remoteAuthorizationDataProvider(
//            AppCoreProp appCoreProp
//            , RestTemplate oauth2ClientRestTemplate
//    ) {
//        log.debug("remoteAuthorizationDataProvider");
////        return new DatabaseAuthorizationDataProvider(userService);
//        return new RemoteAuthorizationDataProvider(appCoreProp, oauth2ClientRestTemplate);
//    }
//
//    @Bean("authorizationDataProvider")
//    @ConditionalOnMissingBean
//    public AuthorizationDataProvider databaseAuthorizationDataProvider(
//            AppCoreProp appCoreProp
//            , ModelMapper modelMapper
////            , UserService userService
//            , UserRepository userRepository
//            , UserPermissionRepository userPermissionRepository
//            , UserViewRepository userViewRepository
//    ) {
//        log.debug("databaseAuthorizationDataProvider");
//        return new DatabaseAuthorizationDataProvider(appCoreProp
//                , modelMapper
////                , userService
//                , userRepository
//                , userPermissionRepository
//                , userViewRepository
//        );
//    }

    @Bean
    public AuthorizationDataProvider authorizationDataProvider(
            AppCoreProp appCoreProp
            , RestTemplate oauth2ClientRestTemplate
    ) {
        log.debug("remoteAuthorizationDataProvider");
//        return new DatabaseAuthorizationDataProvider(userService);
        return new RemoteAuthorizationDataProvider(appCoreProp, oauth2ClientRestTemplate);
    }

    @Bean
    public AuthorizationDataDossierCacheKeyGenerator authorizationDataDossierCacheKeyGenerator() {
        return new AuthorizationDataDossierCacheKeyGenerator();
    }

    @Bean
    public AuthorizationDataPermissionCacheKeyGenerator authorizationDataPermissionCacheKeyGenerator() {
        return new AuthorizationDataPermissionCacheKeyGenerator();
    }

    @Bean
    public AuthorizationDataUserCacheKeyGenerator authorizationDataUserCacheKeyGenerator() {
        return new AuthorizationDataUserCacheKeyGenerator();
    }

}
