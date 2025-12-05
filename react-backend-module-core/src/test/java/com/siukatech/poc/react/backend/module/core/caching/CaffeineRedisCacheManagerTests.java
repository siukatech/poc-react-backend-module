package com.siukatech.poc.react.backend.module.core.caching;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.config.CaffeineRedisCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.handler.CacheExceptionHandler;
import com.siukatech.poc.react.backend.module.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.support.CompositeCacheManager;

import java.util.List;

/**
 * Reference:
 *
 * https://www.baeldung.com/spring-boot-redis-cache
 */
@Slf4j
@SpringBootTest(classes = {
        CacheExceptionHandler.class
        , CaffeineRedisCachingConfig.class
        , AddressService.class
        , ObjectMapper.class
    }
    , properties = {
        "spring.cache.type=caffeine-redis"
//        , "spring.cache.cache-names=test1,test2"
        , "spring.cache.caffeine-redis.time-to-live=2s"
        //
//        , "spring.cache.redis.cache-null-value=false"
        , "spring.data.redis.host=localhost"
        , "spring.data.redis.port=6379"
        , "logging.level.root=INFO"
        , "logging.level.com.siukatech.poc.react.backend.module.core.caching=DEBUG"
    }
)
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class
        , RedisAutoConfiguration.class
})
//@Import({RedisServerConfig.class})
public class CaffeineRedisCacheManagerTests extends AbstractRedisCacheManagerTests {

    @BeforeEach
    public void setup() {
        this.initMemoryAppender(
                List.of(
                        Pair.with(CaffeineRedisCacheManagerTests.class.getPackageName(), Level.DEBUG)
                )
        );
        //
        this.setup_redisServer();
        //
        super.setup_cacheManager();
    }

    @AfterEach
    public void teardown() {
        this.teardown_redisServer();
    }

    @Test
    public void test_caffeineRedisCacheManager_basic() {
        log.debug("test_caffeineRedisCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_caffeineRedisCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);
        //
        CompositeCacheManager compositeCacheManager = (CompositeCacheManager) this.cacheManager;
        log.debug("test_caffeineRedisCacheManager_basic - compositeCacheManager: [{}]", compositeCacheManager);
    }

    @Test
    public void test_getAddressModelById_basic() {
        super.test_getAddressModelById_basic(true);
    }

    @Test
    public void test_getAddressModelById_ttl_exceeded_1s() throws InterruptedException {
        super.test_getAddressModelById_ttl_exceeded_1s(2000L, true);
    }

}
