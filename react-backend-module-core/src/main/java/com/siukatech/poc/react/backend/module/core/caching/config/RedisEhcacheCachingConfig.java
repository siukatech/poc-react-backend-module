package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.handler.DefaultCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.handler.RedisCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.helper.EhcacheCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import com.siukatech.poc.react.backend.module.core.caching.manager.EnhancedCompositeCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.cache.configuration.MutableConfiguration;

@Slf4j
@Configuration
@EnableCaching
@Import({EhcacheCachingHelper.class, RedisCachingHelper.class})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis-ehcache")
public class RedisEhcacheCachingConfig extends AbstractCachingConfig implements CachingConfigurer {

    /**
     * Reference:
     * https://jdriven.com/blog/2024/10/Spring-Boot-Sweets-Using-Duration-Type-With-Configuration-Properties
     */
    @Value("${spring.cache.redis-ehcache.time-to-live:10m}")
    private java.time.Duration timeToLive;

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisEhcacheCachingConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public MutableConfiguration<String, Object> mutableConfiguration(EhcacheCachingHelper ehcacheCachingHelper) {
        log.debug("mutableConfiguration - timeToLive.getSeconds: [{}]", this.timeToLive.getSeconds());
        MutableConfiguration<String, Object> mutableConfiguration = ehcacheCachingHelper
                .resolveMutableConfiguration(this.timeToLive);
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
        return new RedisCacheErrorHandler(redisConnectionFactory);
    }

}
