package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.helper.CaffeineCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Reference:
 * https://baeldung.com/spring-cache-tutorial
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "caffeine-redis")
public class CaffeineRedisCachingConfig extends DefaultCachingConfig {

    @Value("${spring.cache.caffeine-redis.time-to-live:10m}")
    private java.time.Duration timeToLive;

    @Bean(name = "cacheManager")
    public CacheManager caffeineRedisCacheManager(
            RedisConnectionFactory redisConnectionFactory
            , ObjectMapper objectMapper
            , CaffeineCachingHelper caffeineCachingHelper
            , RedisCachingHelper redisCachingHelper
    ) {
        log.debug("caffeineRedisCacheManager - this.getCacheNameListWithDefaults: [{}]"
                , this.getCacheNameListWithDefaults());

        CaffeineCacheManager caffeineCacheManager = caffeineCachingHelper
                .resolveCaffeineCacheManager(timeToLive, this.getCacheNameListWithDefaults());

        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder =
                redisCachingHelper
                        .resolveRedisCacheManagerBuilder(timeToLive
                                , this.getCacheNameListWithDefaults()
                                , redisConnectionFactory, objectMapper
                        );
        RedisCacheManager redisCacheManager = redisCacheManagerBuilder.build();

        CompositeCacheManager compositeCacheManager =
                new CompositeCacheManager(caffeineCacheManager, redisCacheManager);
        compositeCacheManager.setFallbackToNoOpCache(false);

        return caffeineCacheManager;
    }

}
