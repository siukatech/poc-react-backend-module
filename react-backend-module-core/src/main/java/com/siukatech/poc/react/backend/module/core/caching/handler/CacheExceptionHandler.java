package com.siukatech.poc.react.backend.module.core.caching.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheExceptionHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn("handleCacheGetError - cache.getName: [{}], key: [{}]", cache.getName(), key);
        log.warn("handleCacheGetError - exception.message: [{}]", exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn("handleCachePutError - cache.getName: [{}], key: [{}]", cache.getName(), key);
        log.warn("handleCachePutError - exception.message: [{}]", exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("handleCacheEvictError - cache.getName: [{}], key: [{}]", cache.getName(), key);
        log.warn("handleCacheEvictError - exception.message: [{}]", exception.getMessage());
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn("handleCacheClearError - cache.getName: [{}]", cache.getName());
        log.warn("handleCacheClearError - exception.message: [{}]", exception.getMessage());
    }
}
