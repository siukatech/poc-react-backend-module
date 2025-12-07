package com.siukatech.poc.react.backend.module.core.caching;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.caching.config.RedisCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.handler.DefaultCacheErrorHandler;
import com.siukatech.poc.react.backend.module.core.caching.model.AddressModel;
import com.siukatech.poc.react.backend.module.core.caching.model.AddressOptionalModel;
import com.siukatech.poc.react.backend.module.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.SerializationException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Reference:
 * <p>
 * https://www.baeldung.com/spring-boot-redis-cache
 */
@Slf4j
@SpringBootTest(classes = {
        DefaultCacheErrorHandler.class
        , RedisCachingConfig.class
        , AddressService.class
//        , RedisCachingHelper.class
        , ObjectMapper.class
}
        , properties = {
        "spring.cache.type=redis"
//        , "spring.cache.cache-names=test1,test2"
        , "spring.cache.redis.time-to-live=2s"
        , "spring.cache.redis.cache-null-value=false"
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
//public class RedisCacheManagerTests {
public class RedisCacheManagerTests extends AbstractRedisCacheManagerTests {

//    @Autowired
//    private CacheManager cacheManager;

//    @Autowired
//    private AddressService addressService;

    @Value("${spring.cache.redis.time-to-live}")
    private Duration definedTtl;

    @BeforeEach
    public void setup() {
        this.initMemoryAppender(
                List.of(
                        Pair.with(RedisCacheManagerTests.class.getPackageName(), Level.DEBUG)
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

    protected Duration getDefinedTtl() {
        return this.definedTtl;
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
        super.test_xxxCacheManager_basic("RedisCacheManager");

        Assertions.assertTrue(cacheManager.getClass().getSimpleName().toLowerCase()
                .contains("RedisCacheManager".toLowerCase()));
    }

    @Test
    public void test_getAddressModelById_basic() {
        super.test_getAddressModelById_basic(true);
    }

    @Test
    public void test_getAddressModelById_ttl_exceeded_1s() throws InterruptedException {
        super.test_getAddressModelById_ttl_exceeded_1s(this.definedTtl.toMillis(), true);
    }

    @Test
    public void test_addressOptionalModel_withList() {
        // given
        int definedTtl = 2000;
        //
        log.debug("test_addressOptionalModel_withList - saveAddressModel - 1 - before");
        for (int i = 0; i < 5; i++) {
            String iStr = String.valueOf(i);
            AddressModel addressModel01 = new AddressModel(
                    "address-" + iStr
                    , "location-" + iStr
                    , "street-" + iStr
                    , "district-" + iStr
            );
            this.addressService.saveAddressModel(addressModel01);
        }
        log.debug("test_addressOptionalModel_withList - saveAddressModel - 1 - after");

        // when
        log.debug("test_addressOptionalModel_withList - getAddressOptionalModelList - 1 - before");
        List<AddressOptionalModel> addressOptionalModelObjectList = this.addressService.getAddressOptionalModelList("1");
        log.debug("test_addressOptionalModel_withList - getAddressOptionalModelList - 1 - after");

        try {
            log.debug("test_addressOptionalModel_withList - Thread.sleep - 1 - before - definedTtl: [{}]", definedTtl);
            Thread.sleep(definedTtl - 500);
            log.debug("test_addressOptionalModel_withList - Thread.sleep - 1 - after");
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        log.debug("test_addressOptionalModel_withList - getAddressOptionalModelList - 2 - before");
        // Since the cacheErrorHandler is defined, no exception will be thrown
//        Exception exceptionCache01 = Assertions.assertThrows(Exception.class, () -> {
            List<AddressOptionalModel> addressOptionalModelCache01List = this.addressService.getAddressOptionalModelList("2");
//        });
//        Assertions.assertEquals("SerializationException", exceptionCache01.getClass().getSimpleName());
        log.debug("test_addressOptionalModel_withList - getAddressOptionalModelList - 2 - after");

        try {
            log.debug("test_addressOptionalModel_withList - Thread.sleep - 2 - before - definedTtl: [{}]", definedTtl);
            Thread.sleep(definedTtl + 500);
            log.debug("test_addressOptionalModel_withList - Thread.sleep - 2 - after");
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }

        log.debug("test_addressOptionalModel_withList - getAddressOptionalModelList - 3 - before");
        List<AddressOptionalModel> addressOptionalModelCache02List = this.addressService.getAddressOptionalModelList("3");
        log.debug("test_addressOptionalModel_withList - getAddressOptionalModelList - 3 - after");

    }

}
