package com.siukatech.poc.react.backend.module.core.caching.config;

import com.siukatech.poc.react.backend.module.core.caching.handler.RedisCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.helper.CaffeineCachingHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Duration;

/**
 * Reference:
 * https://baeldung.com/spring-cache-tutorial
 */
@Slf4j
@Configuration
@EnableCaching
@Import({CaffeineCachingHelper.class})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "caffeine")
public class CaffeineCachingConfig extends AbstractCachingConfig implements CachingConfigurer {

    @Value("${spring.cache.caffeine.time-to-live:10m}")
    private Duration timeToLive;

    @Bean(name = "cacheManager")
    public CacheManager caffeineCacheManager(CaffeineCachingHelper caffeineCachingHelper) {
        log.debug("caffeineCacheManager - this.getCacheNameListWithDefaults: [{}]"
                , this.getCacheNameListWithDefaults());

        CaffeineCacheManager caffeineCacheManager = caffeineCachingHelper
                .resolveCaffeineCacheManager(this.timeToLive, this.getCacheNameListWithDefaults());

        return caffeineCacheManager;
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
