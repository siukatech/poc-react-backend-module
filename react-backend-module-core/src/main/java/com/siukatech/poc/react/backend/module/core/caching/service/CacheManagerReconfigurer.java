package com.siukatech.poc.react.backend.module.core.caching.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.lang.NonNull;

import javax.cache.configuration.MutableConfiguration;
import java.util.Objects;

@Slf4j
public class CacheManagerReconfigurer {

    public void configure(CacheManager cacheManager
            , MutableConfiguration<String, Object> mutableConfiguration
            , @NonNull String... cacheNames) {
        if (cacheManager instanceof JCacheCacheManager jCacheCacheManager) {
            log.debug("configure - jCacheCacheManager - start");
            for (String cacheName: cacheNames) {
                Objects.requireNonNull(jCacheCacheManager.getCacheManager())
                        .createCache(cacheName, mutableConfiguration);
            }
            log.debug("configure - jCacheCacheManager - end");
        }
        else {
            log.debug("configure - not jCacheCacheManager");
        }
        this.configure(cacheManager, cacheNames);
    }

    public void configure(CacheManager cacheManager
            , @NonNull String... cacheNames) {
        log.debug("configure - not jCacheCacheManager - start");
        for (String cacheName: cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            log.debug("configure - cacheName: [{}], cache: [{}]", cacheName, cache);
        }
        log.debug("configure - not jCacheCacheManager - end");
    }

}
