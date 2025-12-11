package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.helper.EhcacheCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.manager.EnhancedCompositeCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.cache.configuration.MutableConfiguration;
import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
@Import({EhcacheCachingHelper.class, RedisCachingHelper.class})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis-ehcache")
public class RedisEhcacheCachingConfig extends AbstractRedisCachingConfig implements CachingConfigurer {

    /**
     * Reference:
     * https://jdriven.com/blog/2024/10/Spring-Boot-Sweets-Using-Duration-Type-With-Configuration-Properties
     */
    @Value("${spring.cache.redis-ehcache.time-to-live:10m}")
    private java.time.Duration timeToLive;

    public RedisEhcacheCachingConfig(RedisConnectionFactory redisConnectionFactory
            , ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
            , TaskDecorator taskDecorator) {
        super(redisConnectionFactory, threadPoolTaskExecutorBuilder, taskDecorator);
    }

    @Bean
    public MutableConfiguration<String, Object> mutableConfiguration(EhcacheCachingHelper ehcacheCachingHelper) {
        log.debug("mutableConfiguration - timeToLive.getSeconds: [{}]", this.timeToLive.getSeconds());
        Duration ehcacheTimeToLive = Duration.ofSeconds((this.timeToLive.getSeconds() / 2));
        log.debug("mutableConfiguration - timeToLive: [{}], ehcacheTimeToLive: [{}]"
                , this.timeToLive, ehcacheTimeToLive);
        MutableConfiguration<String, Object> mutableConfiguration = ehcacheCachingHelper
                .resolveMutableConfiguration(ehcacheTimeToLive);
        return mutableConfiguration;
    }

    @Primary
    @Bean(name = "cacheManager")
    public CacheManager redisEhcacheCacheManager(
            ObjectMapper objectMapper
//            , RedisConnectionFactory redisConnectionFactory
            , MutableConfiguration<String, Object> mutableConfiguration
            , RedisCachingHelper redisCachingHelper
            , EhcacheCachingHelper ehcacheCachingHelper
    ) {

        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder =
                redisCachingHelper
                        .resolveRedisCacheManagerBuilder(this.timeToLive
                                , this.getCacheNameListWithDefaults()
                                , redisConnectionFactory, objectMapper
                        );
        RedisCacheManager redisCacheManager = redisCacheManagerBuilder.build();

        JCacheCacheManager jCacheCacheManager = ehcacheCachingHelper
                .resolveEhcacheCacheManager(mutableConfiguration
                        , this.getCacheNameListWithDefaults());

        CompositeCacheManager compositeCacheManager =
                new CompositeCacheManager(redisCacheManager, jCacheCacheManager);
        compositeCacheManager.setFallbackToNoOpCache(false);

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
