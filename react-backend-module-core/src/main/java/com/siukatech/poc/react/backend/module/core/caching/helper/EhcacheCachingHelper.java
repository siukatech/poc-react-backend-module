package com.siukatech.poc.react.backend.module.core.caching.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EhcacheCachingHelper {

    public MutableConfiguration<String, Object> resolveMutableConfiguration(Duration timeToLive) {
        log.debug("mutableConfiguration - timeToLive.getSeconds: [{}]", timeToLive.getSeconds());
        MutableConfiguration<String, Object> configuration = new MutableConfiguration<>();
        configuration
                .setExpiryPolicyFactory(
                        CreatedExpiryPolicy.factoryOf(
                                new javax.cache.expiry.Duration(TimeUnit.SECONDS, timeToLive.getSeconds())
                        )
                )
                .setTypes(String.class, Object.class)
                .setStoreByValue(false)
                .setStatisticsEnabled(true)
                .setManagementEnabled(true)
        ;
        return configuration;
    }

    public JCacheCacheManager resolveEhcacheCacheManager(
            MutableConfiguration<String, Object> mutableConfiguration
            , List<String> cacheNameListWithDefaults) {

        CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        javax.cache.CacheManager ehcacheCacheManager = cachingProvider.getCacheManager();
        //
        cacheNameListWithDefaults.forEach(cacheName -> {
            log.debug("resolveEhcacheCacheManager - cacheName: [{}]", cacheName);
            Cache cache = ehcacheCacheManager.getCache(cacheName);
            if (cache == null) {
                ehcacheCacheManager.createCache(cacheName, mutableConfiguration);
            }
        });
        //
        JCacheCacheManager jCacheCacheManager = new JCacheCacheManager(ehcacheCacheManager);
        return jCacheCacheManager;
    }

}
