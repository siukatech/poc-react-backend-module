package com.siukatech.poc.react.backend.module.core.caching.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "ehcache")
public class EhcacheCachingConfig extends DefaultCachingConfig {

    /**
     * Reference:
     * https://jdriven.com/blog/2024/10/Spring-Boot-Sweets-Using-Duration-Type-With-Configuration-Properties
     */
    @Value("${spring.cache.ehcache.time-to-live:10m}")
    private java.time.Duration timeToLive;

    @Bean
    public MutableConfiguration<String, Object> mutableConfiguration() {
        log.debug("ehcacheCacheManager - timeToLive.getSeconds: [{}]", timeToLive.getSeconds());
        MutableConfiguration<String, Object> configuration = new MutableConfiguration<>();
        configuration
                .setExpiryPolicyFactory(
                        CreatedExpiryPolicy.factoryOf(
                                new Duration(TimeUnit.SECONDS, timeToLive.getSeconds())
                        )
                )
                .setTypes(String.class, Object.class)
                .setStoreByValue(false)
                .setStatisticsEnabled(true)
                .setManagementEnabled(true)
        ;
        return configuration;
    }

    @Bean(name = "cacheManager")
    public CacheManager ehcacheCacheManager(MutableConfiguration<String, Object> mutableConfiguration) {
//        CacheManagerBuilder builder = CacheManagerBuilder.newCacheManagerBuilder();
//        org.ehcache.CacheManager ehcacheCacheManager = builder.build();
//        javax.cache.CacheManager jCacheManager = ehcacheCacheManager.
//        CacheManager cacheManager = new JCacheCacheManager(ehcacheCacheManager);
        //
        CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        javax.cache.CacheManager ehcacheCacheManager = cachingProvider.getCacheManager();
        //
        this.getCacheNameListWithDefaults().forEach(cacheName -> {
            log.debug("ehcacheCacheManager - cacheName: [{}]", cacheName);
            ehcacheCacheManager.createCache(cacheName, mutableConfiguration);
        });
        //
        JCacheCacheManager jCacheCacheManager = new JCacheCacheManager(ehcacheCacheManager);
        return jCacheCacheManager;
    }

}
