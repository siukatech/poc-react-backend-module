package com.siukatech.poc.react.backend.module.user.caching;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCachingConfig {

    public static final String CACHE_NAME_USER = "user";

    @Bean
    public UserPermissionCacheKeyGenerator userPermissionCacheKeyGenerator() {
        return new UserPermissionCacheKeyGenerator();
    }
}
