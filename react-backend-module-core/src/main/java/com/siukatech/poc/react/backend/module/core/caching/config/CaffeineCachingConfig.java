package com.siukatech.poc.react.backend.module.core.caching.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.siukatech.poc.react.backend.module.core.caching.helper.CaffeineCachingHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Reference:
 * https://baeldung.com/spring-cache-tutorial
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "caffeine")
public class CaffeineCachingConfig extends DefaultCachingConfig {

    @Value("${spring.cache.caffeine.time-to-live:10m}")
    private Duration timeToLive;

    private final CaffeineCachingHelper caffeineCachingHelper;

    public CaffeineCachingConfig(CaffeineCachingHelper caffeineCachingHelper) {
        this.caffeineCachingHelper = caffeineCachingHelper;
    }

    @Bean(name = "cacheManager")
    public CacheManager caffeineCacheManager() {
        log.debug("caffeineCacheManager - this.getCacheNameListWithDefaults: [{}]"
                , this.getCacheNameListWithDefaults());

        CaffeineCacheManager caffeineCacheManager = this.caffeineCachingHelper
                .resolveCaffeineCacheManager(this.timeToLive, this.getCacheNameListWithDefaults());

        return caffeineCacheManager;
    }

}
