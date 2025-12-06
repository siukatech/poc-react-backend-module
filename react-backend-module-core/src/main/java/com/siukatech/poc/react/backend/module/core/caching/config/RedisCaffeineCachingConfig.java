package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.handler.DefaultCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.helper.CaffeineCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.manager.EnhancedCompositeCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Reference:
 * https://baeldung.com/spring-cache-tutorial
 */
@Slf4j
@Configuration
@EnableCaching
@Import({CaffeineCachingHelper.class, RedisCachingHelper.class})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis-caffeine")
public class RedisCaffeineCachingConfig extends DefaultCachingConfig {

    @Value("${spring.cache.redis-caffeine.time-to-live:10m}")
    private java.time.Duration timeToLive;

    public RedisCaffeineCachingConfig(DefaultCacheErrorHandler defaultCacheErrorHandler) {
        super(defaultCacheErrorHandler);
    }

    @Primary
    @Bean(name = "cacheManager")
    public CacheManager redisCaffeineCacheManager(
            RedisConnectionFactory redisConnectionFactory
            , ObjectMapper objectMapper
            , RedisCachingHelper redisCachingHelper
            , CaffeineCachingHelper caffeineCachingHelper
    ) {
        log.debug("redisCaffeineCacheManager - this.getCacheNameListWithDefaults: [{}]"
                , this.getCacheNameListWithDefaults());

        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder =
                redisCachingHelper
                        .resolveRedisCacheManagerBuilder(this.timeToLive
                                , this.getCacheNameListWithDefaults()
                                , redisConnectionFactory, objectMapper
                        );
        RedisCacheManager redisCacheManager = redisCacheManagerBuilder.build();

        CaffeineCacheManager caffeineCacheManager = caffeineCachingHelper
                .resolveCaffeineCacheManager(this.timeToLive, this.getCacheNameListWithDefaults());

        CompositeCacheManager compositeCacheManager =
                new CompositeCacheManager(redisCacheManager, caffeineCacheManager);
        compositeCacheManager.setFallbackToNoOpCache(false);

        redisCacheManager.getCacheNames().forEach(cacheName -> {
            log.info("redisCaffeineCacheManager - initialized - cacheName: [{}]", cacheName);
        });

//        return compositeCacheManager;
        return new EnhancedCompositeCacheManager(compositeCacheManager);
    }

}
