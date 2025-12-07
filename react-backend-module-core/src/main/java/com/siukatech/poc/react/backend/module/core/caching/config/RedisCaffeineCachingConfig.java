package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.handler.DefaultCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.handler.RedisCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.helper.CaffeineCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.manager.EnhancedCompositeCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
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
public class RedisCaffeineCachingConfig extends AbstractCachingConfig implements CachingConfigurer {

    @Value("${spring.cache.redis-caffeine.time-to-live:10m}")
    private java.time.Duration timeToLive;

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisCaffeineCachingConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Primary
    @Bean(name = "cacheManager")
    public CacheManager redisCaffeineCacheManager(
            ObjectMapper objectMapper
//            , RedisConnectionFactory redisConnectionFactory
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

    // Using Bean without CacheConfigurer is not working
//    @Bean("errorHandler")
//    public CacheErrorHandler cacheErrorHandler(RedisConnectionFactory redisConnectionFactory) {
//        return new RedisCacheErrorHandler(redisConnectionFactory);
//    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler(redisConnectionFactory);
    }

}
