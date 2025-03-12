package com.siukatech.poc.react.backend.module.core.caching.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

@Slf4j
@TestConfiguration
public class TestRedisConfig {

    private RedisServer redisServer;

    public TestRedisConfig(RedisProperties redisProperties) throws IOException {
        log.info("TestRedisConfig - redisProperties: [{}]", redisProperties);
        this.redisServer = new RedisServer(redisProperties.getPort());
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        log.info("postConstruct - redisServer.starting");
        redisServer.start();
        log.info("postConstruct - redisServer.started");
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        log.info("preDestroy - redisServer.stopping");
        redisServer.stop();
        log.info("preDestroy - redisServer.stopped");
    }
}
