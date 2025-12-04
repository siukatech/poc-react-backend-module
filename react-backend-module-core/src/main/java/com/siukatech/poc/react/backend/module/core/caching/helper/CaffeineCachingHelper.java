package com.siukatech.poc.react.backend.module.core.caching.helper;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class CaffeineCachingHelper {

    public CaffeineCacheManager resolveCaffeineCacheManager(
            Duration timeToLive, List<String> cacheNameListWithDefaults) {
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                .expireAfterWrite(timeToLive)
                .maximumSize(10_000)
                .recordStats()
                ;

        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeineBuilder);
        caffeineCacheManager.setCacheNames(cacheNameListWithDefaults);
        return caffeineCacheManager;
    }

}
