package com.siukatech.poc.react.backend.module.user.caching;

import com.siukatech.poc.react.backend.module.core.caching.service.CacheManagerReconfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;

@Slf4j
@Configuration
public class UserCachingConfig {

    public static final String CACHE_NAME_USER = "user";

    @Bean
    public UserPermissionCacheKeyGenerator userPermissionCacheKeyGenerator() {
        return new UserPermissionCacheKeyGenerator();
    }

    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "ehcache")
    @Bean
    public CacheManagerReconfigurer userEhcacheCacheManagerReconfigurer(
            CacheManager cacheManager, MutableConfiguration<String, Object> mutableConfiguration) {
        CacheManagerReconfigurer cacheManagerReconfigurer = new CacheManagerReconfigurer();
        cacheManagerReconfigurer.configure(cacheManager, mutableConfiguration
                , UserCachingConfig.CACHE_NAME_USER);
        return cacheManagerReconfigurer;
    }

    @ConditionalOnExpression("'${spring.cache.type}'.equals('redis') || '${spring.cache.type}'.equals('simple')")
//    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
    @Bean
    public CacheManagerReconfigurer userRedisOrSimpleCacheManagerReconfigurer(CacheManager cacheManager) {
        log.debug("userRedisOrSimpleCacheManagerReconfigurer");
        CacheManagerReconfigurer cacheManagerReconfigurer = new CacheManagerReconfigurer();
        cacheManagerReconfigurer.configure(cacheManager, UserCachingConfig.CACHE_NAME_USER);
        return cacheManagerReconfigurer;
    }

//    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "simple")
//    @Bean
//    public CacheManagerReconfigurer userSimpleCacheManagerReconfigurer(CacheManager cacheManager) {
//        CacheManagerReconfigurer cacheManagerReconfigurer = new CacheManagerReconfigurer();
//        cacheManagerReconfigurer.configure(cacheManager, UserCachingConfig.CACHE_NAME_USER);
//        return cacheManagerReconfigurer;
//    }

}
