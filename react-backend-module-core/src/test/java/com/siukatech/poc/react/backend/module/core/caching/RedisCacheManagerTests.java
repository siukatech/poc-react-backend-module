package com.siukatech.poc.react.backend.module.core.caching;

import ch.qos.logback.classic.Level;
import com.siukatech.poc.react.backend.module.core.caching.config.RedisCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.redis.TestRedisConfig;
import com.siukatech.poc.react.backend.module.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.List;
import java.util.Map;

/**
 * Reference:
 *
 * https://www.baeldung.com/spring-boot-redis-cache
 */
@Slf4j
@SpringBootTest(classes = {RedisCachingConfig.class
        , AddressService.class
    }
    , properties = {
        "spring.cache.type=redis"
//        , "spring.cache.cache-names=test1,test2"
        , "spring.cache.redis.time-to-live=1s"
        , "spring.cache.redis.cache-null-value=false"
        , "spring.data.redis.host=localhost"
        , "spring.data.redis.port=6379"
        , "logging.level.root=INFO"
        , "logging.level.com.siukatech.poc.react.backend.core.caching=DEBUG"
    }
)
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class
        , RedisAutoConfiguration.class
})
@Import(value = {TestRedisConfig.class})
//public class RedisCacheManagerTests {
public class RedisCacheManagerTests extends AbstractCachingManagerTests {

//    @Autowired
//    private CacheManager cacheManager;

//    @Autowired
//    private AddressService addressService;

    @BeforeEach
    public void setup() {
        this.initMemoryAppender(
                List.of(
                        Pair.with(RedisCacheManagerTests.class.getPackageName(), Level.DEBUG)
                )
        );
        //
        super.setup_cacheManager();
    }

    @Test
    public void test_redisCacheManager_basic() {
        log.debug("test_redisCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_redisCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);
        //
        RedisCacheManager redisCacheManager = (RedisCacheManager) this.cacheManager;
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = redisCacheManager.getCacheConfigurations();
        log.debug("test_redisCacheManager_basic - redisCacheConfigurationMap: [{}]", redisCacheConfigurationMap);
        redisCacheConfigurationMap.forEach((k, v) -> {
            log.debug("test_redisCacheManager_basic - redisCacheConfigurationMap - k: [{}], v: [{}]", k, v);
            log.debug("test_redisCacheManager_basic - redisCacheConfigurationMap - v.getAllowCacheNullValues: [{}]", v.getAllowCacheNullValues());
            log.debug("test_redisCacheManager_basic - redisCacheConfigurationMap - v.getKeyPrefix: [{}]", v.getKeyPrefix());
        });
        //
//        super.test_xxxCacheManager_basic("RedisCacheManager");
    }

    @Test
    public void test_getAddressModelById_basic() {
        super.test_getAddressModelById_basic();
    }

    @Test
    public void test_getAddressModelById_ttl_exceeded_1s() throws InterruptedException {
        super.test_getAddressModelById_ttl_exceeded_1s(1000L);
    }

}
