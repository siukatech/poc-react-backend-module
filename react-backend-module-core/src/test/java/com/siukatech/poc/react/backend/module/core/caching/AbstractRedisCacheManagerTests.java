package com.siukatech.poc.react.backend.module.core.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.embedded.RedisServer;

@Slf4j
public abstract class AbstractRedisCacheManagerTests extends AbstractCachingManagerTests {

    @Autowired
    protected RedisProperties redisProperties;

    protected RedisServer redisServer;

    protected void setup_redisServer() {
        try {
            this.redisServer = RedisServer.newRedisServer().port(redisProperties.getPort()).build();
            this.redisServer.start();
        } catch (Exception e) {
            log.error("setup_redisServer", e.fillInStackTrace());
        }
    }

    protected void teardown_redisServer() {
        try {
            this.redisServer.stop();
        } catch (Exception e) {
            log.error("teardown_redisServer", e.fillInStackTrace());
        }
    }

}
