package com.siukatech.poc.react.backend.module.core.caching.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@Slf4j
@Configuration
@EnableCaching
@EnableRedisRepositories
@Import({RedisCachingHelper.class})
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisCachingConfig extends AbstractRedisCachingConfig implements CachingConfigurer {

    @Value("${spring.cache.redis.time-to-live:10m}")
    private java.time.Duration timeToLive;

//    @Getter
//    @ConfigurationProperties(prefix = "spring.cache")
//    public static class RedisCacheProp {
//        private String host;
//        private int port;
//    }

    public RedisCachingConfig(RedisConnectionFactory redisConnectionFactory
            , ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder
            , TaskDecorator taskDecorator) {
        super(redisConnectionFactory, threadPoolTaskExecutorBuilder, taskDecorator);
    }

    @Bean(name = "cacheManager")
    public CacheManager redisCacheManager(
            ObjectMapper objectMapper
//            , RedisConnectionFactory redisConnectionFactory
            , RedisCachingHelper redisCachingHelper
    ) {
        log.debug("redisCacheManager - timeToLive.getSeconds: [{}]", this.timeToLive.getSeconds());
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(this.timeToLive);
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
//                .fromConnectionFactory(redisConnectionFactory)
//                .cacheDefaults(redisCacheConfiguration)
//                .allowCreateOnMissingCache(true)
//                ;
////        this.getCacheNameListWithDefaults().forEach(cacheName -> {
////            log.debug("redisCacheManager - cacheName: [{}]", cacheName);
////            builder.withCacheConfiguration(cacheName, redisCacheConfiguration);
////        });

        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder =
                redisCachingHelper
                        .resolveRedisCacheManagerBuilder(this.timeToLive
                                , this.getCacheNameListWithDefaults()
                                , redisConnectionFactory, objectMapper
                        );
        RedisCacheManager redisCacheManager = redisCacheManagerBuilder.build();
        return redisCacheManager;
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
