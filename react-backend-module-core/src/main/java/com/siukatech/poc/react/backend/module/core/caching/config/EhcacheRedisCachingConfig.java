//package com.siukatech.poc.react.backend.module.core.caching.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.siukatech.poc.react.backend.module.core.caching.handler.CacheExceptionHandler;
//import com.siukatech.poc.react.backend.module.core.caching.helper.EhcacheCachingHelper;
//import com.siukatech.poc.react.backend.module.core.caching.helper.RedisCachingHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.jcache.JCacheCacheManager;
//import org.springframework.cache.support.CompositeCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//
//import javax.cache.configuration.MutableConfiguration;
//
//@Slf4j
//@Configuration
//@EnableCaching
//@Import({EhcacheCachingHelper.class, RedisCachingHelper.class})
//@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "ehcache-redis")
//public class EhcacheRedisCachingConfig extends DefaultCachingConfig {
//
//    /**
//     * Reference:
//     * https://jdriven.com/blog/2024/10/Spring-Boot-Sweets-Using-Duration-Type-With-Configuration-Properties
//     */
//    @Value("${spring.cache.ehcache-redis.time-to-live:10m}")
//    private java.time.Duration timeToLive;
//
//    public EhcacheRedisCachingConfig(CacheExceptionHandler cacheExceptionHandler) {
//        super(cacheExceptionHandler);
//    }
//
//    @Bean
//    public MutableConfiguration<String, Object> mutableConfiguration(EhcacheCachingHelper ehcacheCachingHelper) {
//        log.debug("mutableConfiguration - timeToLive.getSeconds: [{}]", this.timeToLive.getSeconds());
//        MutableConfiguration<String, Object> mutableConfiguration = ehcacheCachingHelper
//                .resolveMutableConfiguration(this.timeToLive);
//        return mutableConfiguration;
//    }
//
//    @Primary
//    @Bean(name = "cacheManager")
//    public CacheManager ehcacheRedisCacheManager(
//            MutableConfiguration<String, Object> mutableConfiguration
//            , RedisConnectionFactory redisConnectionFactory
//            , ObjectMapper objectMapper
//            , EhcacheCachingHelper ehcacheCachingHelper
//            , RedisCachingHelper redisCachingHelper
//    ) {
//        JCacheCacheManager jCacheCacheManager = ehcacheCachingHelper
//                .resolveEhcacheCacheManager(mutableConfiguration
//                        , this.getCacheNameListWithDefaults());
//
//        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder =
//                redisCachingHelper
//                        .resolveRedisCacheManagerBuilder(this.timeToLive
//                                , this.getCacheNameListWithDefaults()
//                                , redisConnectionFactory, objectMapper
//                        );
//        RedisCacheManager redisCacheManager = redisCacheManagerBuilder.build();
//
//        CompositeCacheManager compositeCacheManager =
//                new CompositeCacheManager(jCacheCacheManager, redisCacheManager);
////                new CompositeCacheManager(redisCacheManager, jCacheCacheManager);
//        compositeCacheManager.setFallbackToNoOpCache(false);
//
//        return compositeCacheManager;
//    }
//
//}
