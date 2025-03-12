package com.siukatech.poc.react.backend.module.core.caching.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RedisClientTests {

//    @Test
    public void test_redisClient_basic() {
        String host = "localhost";
        int port = 36379;
        String password = "redispass";
        RedisURI redisURI = RedisURI.builder()
                .withHost(host)
                .withPort(port)
                .withPassword(password.toCharArray())
                .build();
        try (RedisClient redisClient = RedisClient.create(redisURI)) {
            redisClient.connect();
        }
    }

}
