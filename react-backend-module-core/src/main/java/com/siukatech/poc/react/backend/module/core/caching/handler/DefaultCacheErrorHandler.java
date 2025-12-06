package com.siukatech.poc.react.backend.module.core.caching.handler;

import io.netty.handler.timeout.TimeoutException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(@NonNull RuntimeException exception, Cache cache, Object key) {
        CacheErrorInfo cacheErrorInfo =  this.resolveCacheErrorInfo(exception);
        if (cacheErrorInfo.isRedisTimeoutOrConnectionError()) {
            log.warn("handleCacheGetError - exceptionName: [{}], exception.message: [{}], cache.getName: [{}], key: [{}]"
                    , cacheErrorInfo.getExceptionName(), exception.getMessage(), cache.getName(), key);
        }
        else {
            log.error("handleCacheGetError - exception.message: [{}]", exception.getMessage(), exception);
        }
    }

    @Override
    public void handleCachePutError(@NonNull RuntimeException exception, Cache cache, Object key, Object value) {
        CacheErrorInfo cacheErrorInfo =  this.resolveCacheErrorInfo(exception);
        if (cacheErrorInfo.isRedisTimeoutOrConnectionError()) {
            log.warn("handleCachePutError - exceptionName: [{}], exception.message: [{}], cache.getName: [{}], key: [{}]"
                    , cacheErrorInfo.getExceptionName(), exception.getMessage(), cache.getName(), key);
        }
        else {
            log.error("handleCachePutError - exception.message: [{}]", exception.getMessage(), exception);
        }
    }

    @Override
    public void handleCacheEvictError(@NonNull RuntimeException exception, Cache cache, Object key) {
        CacheErrorInfo cacheErrorInfo =  this.resolveCacheErrorInfo(exception);
        if (cacheErrorInfo.isRedisTimeoutOrConnectionError()) {
            log.warn("handleCacheEvictError - exceptionName: [{}], exception.message: [{}], cache.getName: [{}], key: [{}]"
                    , cacheErrorInfo.getExceptionName(), exception.getMessage(), cache.getName(), key);
        }
        else {
            log.error("handleCacheEvictError - exception.message: [{}]", exception.getMessage(), exception);
        }
    }

    @Override
    public void handleCacheClearError(@NonNull RuntimeException exception, Cache cache) {
        CacheErrorInfo cacheErrorInfo =  this.resolveCacheErrorInfo(exception);
        if (cacheErrorInfo.isRedisTimeoutOrConnectionError()) {
            log.warn("handleCacheClearError - exceptionName: [{}], exception.message: [{}], cache.getName: [{}]"
                    , cacheErrorInfo.getExceptionName(), exception.getMessage(), cache.getName());
        }
        else {
            log.error("handleCacheClearError - exception.message: [{}]", exception.getMessage(), exception);
        }
    }

    private boolean isRedisTimeoutOrConnectionError(Throwable t) {
        return t instanceof RedisConnectionFailureException
                || t instanceof RedisSystemException
                || t instanceof QueryTimeoutException
                || t instanceof TimeoutException
                || (t.getMessage() != null && t.getMessage().contains("timeout"));
    }

    private CacheErrorInfo resolveCacheErrorInfo(RuntimeException exception) {
        CacheErrorInfo cacheErrorInfo = new CacheErrorInfo(
                this.isRedisTimeoutOrConnectionError(exception)
                , exception.getClass().getSimpleName()
        );
        log.debug("resolveCacheErrorInfo - cacheErrorInfo: [{}]", cacheErrorInfo);
        return cacheErrorInfo;
    }

    @Data
    @AllArgsConstructor
    private static class CacheErrorInfo {
        private boolean isRedisTimeoutOrConnectionError;
        private String exceptionName;
    }

}
