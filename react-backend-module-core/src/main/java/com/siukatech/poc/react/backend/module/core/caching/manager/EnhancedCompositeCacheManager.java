package com.siukatech.poc.react.backend.module.core.caching.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class EnhancedCompositeCacheManager implements CacheManager {

    private final CompositeCacheManager delegate;

    // Track per-cache failure state
    private final Set<String> redisFailedCaches = ConcurrentHashMap.newKeySet();

    @Override
    public Cache getCache(String name) {
        Cache cache = delegate.getCache(name);
        if (cache == null) return null;
        return new LoggingCacheWrapper(cache, name);
    }

    @Override public Collection<String> getCacheNames() { return delegate.getCacheNames(); }

    private class LoggingCacheWrapper implements Cache {
        private final Cache delegate;
        private final String cacheName;

        LoggingCacheWrapper(Cache delegate, String cacheName) {
            this.delegate = delegate;
            this.cacheName = cacheName;
        }

        public ValueWrapper get(Object key) {
            try {
                ValueWrapper valueWrapper = delegate.get(key);
                boolean redisFailedCachesRemovalResult = redisFailedCaches.remove(cacheName);
                log.debug("LoggingCacheWrapper.get - redisFailedCachesRemovalResult: [{}], cacheName: [{}]"
                        , redisFailedCachesRemovalResult, cacheName);
                if (redisFailedCachesRemovalResult) {
                    log.info("LoggingCacheWrapper.get - REDIS RECOVERED → back to primary Redis | cache: [{}]", cacheName);
                }
                return valueWrapper;
            }
            catch (Exception e) {
                boolean isRedisConnectionError = isRedisConnectionError(e);
                boolean redisFailedCachesAdditionResult = redisFailedCaches.add(cacheName);
                log.debug("LoggingCacheWrapper.get - isRedisConnectionError: [{}], redisFailedCachesAdditionResult: [{}]", isRedisConnectionError, redisFailedCachesAdditionResult);
                if (isRedisConnectionError(e) && redisFailedCachesAdditionResult) {
                    log.warn("LoggingCacheWrapper.get - REDIS DOWN → switched to local Caffeine fallback | cache: [{}] key: [{}]", cacheName, key);
                }
                else {
                    log.warn("LoggingCacheWrapper.get - REDIS IS STILL DOWN → using local Caffeine as backup | cache: [{}] key: [{}]", cacheName, key);
                }
                throw e; // Let CacheErrorHandler swallow it
            }
        }

        // Forward all other methods
        @Override @NonNull public String getName() { return delegate.getName(); }
        @Override @NonNull public Object getNativeCache() { return delegate.getNativeCache(); }
        @Override public <T> T get(@NonNull Object key, Class<T> type) { return delegate.get(key, type); }
        @Override public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) { return delegate.get(key, valueLoader); }
        @Override public void put(@NonNull Object key, Object value) { delegate.put(key, value); }
        @Override public ValueWrapper putIfAbsent(@NonNull Object key, Object value) { return delegate.putIfAbsent(key, value); }
        @Override public void evict(@NonNull Object key) { delegate.evict(key); }
        @Override public boolean evictIfPresent(@NonNull Object key) { return delegate.evictIfPresent(key); }
        @Override public void clear() { delegate.clear(); }
        @Override public boolean invalidate() { return delegate.invalidate(); }
    }

    private boolean isRedisConnectionError(Throwable t) {
        boolean isInstanceofRedisConnectionFailureException = t instanceof RedisConnectionFailureException;
        boolean isClassNameContainingRedis = t.getClass().getName().toLowerCase().contains("redis");
        boolean isMessageContainingRedis = (t.getMessage() != null && t.getMessage().toLowerCase().contains("redis"));
        log.debug("isRedisConnectionError - isInstanceofRedisConnectionFailureException: [{}], isClassNameContainingRedis: [{}], isMessageContainingRedis: [{}]"
                , isInstanceofRedisConnectionFailureException, isClassNameContainingRedis, isMessageContainingRedis);
//        return t instanceof RedisConnectionFailureException ||
//                t.getClass().getName().contains("Redis") ||
//                (t.getMessage() != null && t.getMessage().toLowerCase().contains("redis"));
        return isInstanceofRedisConnectionFailureException || isClassNameContainingRedis || isMessageContainingRedis;
    }
}
