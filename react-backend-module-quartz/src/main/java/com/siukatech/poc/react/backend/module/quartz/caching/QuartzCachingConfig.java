package com.siukatech.poc.react.backend.module.quartz.caching;

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
public class QuartzCachingConfig {

    public static final String CACHE_NAME_QUARTZ = "quartz";

//    @Bean
//    public UserPermissionCacheKeyGenerator userPermissionCacheKeyGenerator() {
//        return new UserPermissionCacheKeyGenerator();
//    }

    @ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "ehcache")
    @Bean
    public CacheManagerReconfigurer userEhcacheCacheManagerReconfigurer(
            CacheManager cacheManager, MutableConfiguration<String, Object> mutableConfiguration) {
        CacheManagerReconfigurer cacheManagerReconfigurer = new CacheManagerReconfigurer();
        cacheManagerReconfigurer.configure(cacheManager, mutableConfiguration
                , QuartzCachingConfig.CACHE_NAME_QUARTZ);
        return cacheManagerReconfigurer;
    }

    @ConditionalOnExpression("'${spring.cache.type}'.equals('redis') || '${spring.cache.type}'.equals('simple')")
    @Bean
    public CacheManagerReconfigurer quartzRedisOrSimpleCacheManagerReconfigurer(CacheManager cacheManager) {
        log.debug("quartzRedisOrSimpleCacheManagerReconfigurer");
        CacheManagerReconfigurer cacheManagerReconfigurer = new CacheManagerReconfigurer();
        cacheManagerReconfigurer.configure(cacheManager, QuartzCachingConfig.CACHE_NAME_QUARTZ);
        return cacheManagerReconfigurer;
    }

}
