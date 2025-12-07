package com.siukatech.poc.react.backend.module.core.caching.handler;

import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.lang.NonNull;

@Slf4j
@RequiredArgsConstructor
public class DefaultCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(@NonNull RuntimeException exception, Cache cache, Object key) {
        this.handleCacheError("handleCacheGetError", exception, cache, key);
    }

    @Override
    public void handleCachePutError(@NonNull RuntimeException exception, Cache cache, Object key, Object value) {
        this.handleCacheError("handleCachePutError", exception, cache, key);
    }

    @Override
    public void handleCacheEvictError(@NonNull RuntimeException exception, Cache cache, Object key) {
        this.handleCacheError("handleCacheEvictError", exception, cache, key);
    }

    @Override
    public void handleCacheClearError(@NonNull RuntimeException exception, Cache cache) {
        this.handleCacheError("handleCacheClearError", exception, cache, null);
    }

    private void handleCacheError(String label, @NonNull RuntimeException exception, Cache cache, Object key) {
        CacheErrorInfo cacheErrorInfo =  this.resolveCacheErrorInfo(exception);
        if (cacheErrorInfo.isRedisTimeoutOrConnectionError()) {
            log.warn("{} - exceptionName: [{}], exception.message: [{}], cache.getName: [{}], key: [{}]"
                    , label, cacheErrorInfo.getExceptionName(), exception.getMessage(), cache.getName(), key);
            if (exception instanceof QueryTimeoutException queryTimeoutException) {
                this.handleQueryTimeoutException(queryTimeoutException, cache);
            }
        }
        else {
            log.error("{} - exception.message: [{}]", label, exception.getMessage(), exception);
        }
    }

    protected void handleQueryTimeoutException(QueryTimeoutException exception, Cache cache) {
        log.debug("handleQueryTimeoutException - do nothing, cache: [{}]", cache.getName());
    }

    protected boolean isRedisTimeoutOrConnectionError(Throwable t) {
        return t instanceof RedisConnectionFailureException
                || t instanceof RedisSystemException
                || t instanceof QueryTimeoutException
                || t instanceof TimeoutException
                || (t.getMessage() != null && t.getMessage().contains("timeout"));
    }

    protected CacheErrorInfo resolveCacheErrorInfo(RuntimeException exception) {
        CacheErrorInfo cacheErrorInfo = new CacheErrorInfo(
                this.isRedisTimeoutOrConnectionError(exception)
                , exception.getClass().getSimpleName()
        );
        log.debug("resolveCacheErrorInfo - cacheErrorInfo: [{}]", cacheErrorInfo);
        return cacheErrorInfo;
    }

}
