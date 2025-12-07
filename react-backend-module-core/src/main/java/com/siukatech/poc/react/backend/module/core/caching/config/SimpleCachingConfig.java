package com.siukatech.poc.react.backend.module.core.caching.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reference:
 * https://baeldung.com/spring-cache-tutorial
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "simple")
public class SimpleCachingConfig extends AbstractCachingConfig implements CachingConfigurer {

    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return (cacheManager) -> cacheManager.setAllowNullValues(false);
    }

    @Bean(name = "cacheManager")
    public CacheManager simpleCacheManager() {
        log.debug("simpleCacheManager - this.getCacheNameListWithDefaults: [{}]"
                , this.getCacheNameListWithDefaults());
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager(
//                this.getCacheNameListWithDefaults().toArray(String[]::new)
        );
        return concurrentMapCacheManager;
    }

    // Using Bean without CacheConfigurer is not working
//    @Bean
//    public CacheErrorHandler cacheErrorHandler() {
//        return this.defaultCacheErrorHandler();
//    }

    @Override
    public CacheErrorHandler errorHandler() {
        return defaultCacheErrorHandler();
    }

}
