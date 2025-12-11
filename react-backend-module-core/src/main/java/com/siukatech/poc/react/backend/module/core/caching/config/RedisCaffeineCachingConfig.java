package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.helper.CaffeineCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.manager.EnhancedCompositeCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
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
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * Reference:
 * https://baeldung.com/spring-cache-tutorial
 */
@Slf4j
@Configuration
@EnableCaching
@Import({CaffeineCachingHelper.class, RedisCachingHelper.class})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis-caffeine")
public class RedisCaffeineCachingConfig extends AbstractRedisCachingConfig implements CachingConfigurer {

    @Value("${spring.cache.redis-caffeine.time-to-live:10m}")
    private java.time.Duration timeToLive;

    public RedisCaffeineCachingConfig(RedisConnectionFactory redisConnectionFactory
            , ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
            , TaskDecorator taskDecorator) {
        super(redisConnectionFactory, threadPoolTaskExecutorBuilder, taskDecorator);
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

        Duration caffeineTimeToLive = Duration.ofSeconds((this.timeToLive.getSeconds() / 2));
        log.debug("redisCaffeineCacheManager - timeToLive: [{}], caffeineTimeToLive: [{}]"
                , this.timeToLive, caffeineTimeToLive);
        CaffeineCacheManager caffeineCacheManager = caffeineCachingHelper
                .resolveCaffeineCacheManager(caffeineTimeToLive, this.getCacheNameListWithDefaults());

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
        return this.resolveCacheErrorHandler();
    }

}
