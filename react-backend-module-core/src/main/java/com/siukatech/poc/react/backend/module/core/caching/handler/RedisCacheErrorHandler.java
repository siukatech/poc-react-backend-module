package com.siukatech.poc.react.backend.module.core.caching.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class RedisCacheErrorHandler extends DefaultCacheErrorHandler {

    private final RedisConnectionFactory redisConnectionFactory;

    private final Set<String> recentlyResetCacheNameSet = ConcurrentHashMap.newKeySet();

    private static final ExecutorService RESET_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "redis-pool-reset-thread");
        thread.setDaemon(true);
        return thread;
    });

    public RedisCacheErrorHandler(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    protected void handleQueryTimeoutException(QueryTimeoutException exception, Cache cache) {
        String cacheName = cache.getName();
        boolean recentlyResetCacheNameSetAdditionResult = this.recentlyResetCacheNameSet.add(cacheName);
        if (recentlyResetCacheNameSetAdditionResult) {
            log.warn("handleQueryTimeoutException - QueryTimeoutException detected - hard reset Lettuce connection pool start | cache: [{}]", cacheName);
            this.resetLettucePoolAsync("handleQueryTimeoutException");

            // Not reset within 5 seconds
            Runnable resetFunc = () -> {
                try { Thread.sleep(5000); } catch (Exception ignored) {}
                boolean recentlyResetCacheNameSetRemovalResult = recentlyResetCacheNameSet.remove(cacheName);
                log.debug("handleQueryTimeoutException - resetFunc - cache: [{}], recentlyResetCacheNameSetRemovalResult: [{}]", cacheName, recentlyResetCacheNameSetRemovalResult);
            };
//            Thread.startVirtualThread(resetFunc);
            RESET_EXECUTOR.submit(resetFunc);
            log.warn("handleQueryTimeoutException - QueryTimeoutException detected - hard reset Lettuce connection pool end | cache: [{}]", cacheName);
        }
    }

    private void resetLettucePoolAsync(String label) {
        Runnable resetFunc = () -> {
            log.info("{} - resetLettucePoolAsync - reset Redis connection pool start", label);
            try {
                if (this.redisConnectionFactory instanceof LettuceConnectionFactory lettuceConnectionFactory) {
                    log.info("{} - resetLettucePoolAsync - Lettuce connection reset start", label);
                    lettuceConnectionFactory.resetConnection();
                    log.info("{} - resetLettucePoolAsync - Lettuce connection close start", label);
                    lettuceConnectionFactory.getConnection().close();
                    log.info("{} - resetLettucePoolAsync - Lettuce connection pool has been reset", label);
                }
                else {
                    log.info("{} - resetLettucePoolAsync - Other redis connection close start", label);
                    this.redisConnectionFactory.getConnection().close();
                    log.info("{} - resetLettucePoolAsync - Other redis connection has been closed", label);
                }
            }
            catch (Exception exception) {
                log.error("{} - resetLettucePoolAsync - exception.getMessage: [{}]"
                        , label, exception.getMessage(), exception.fillInStackTrace());
            }
            log.info("{} - resetLettucePoolAsync - reset Redis connection pool end", label);
        };
//        Thread.startVirtualThread(resetFunc);
        RESET_EXECUTOR.submit(resetFunc);
    }

}
