package com.siukatech.poc.react.backend.module.core.caching;

import ch.qos.logback.classic.Level;
import com.siukatech.poc.react.backend.module.core.caching.config.SimpleCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.handler.CacheExceptionHandler;
import com.siukatech.poc.react.backend.module.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest(classes = {
        CacheExceptionHandler.class
        , SimpleCachingConfig.class
        , AddressService.class
    }
    , properties = {
        "spring.cache.type=simple"
        , "spring.cache.cache-names=test1,test2"
//        , "spring.cache.redis.time-to-live=100s"
        , "logging.level.root=INFO"
        , "logging.level.com.siukatech.poc.react.backend.module.core.caching=DEBUG"
    }
)
//@ImportAutoConfiguration(classes = {
//    CacheAutoConfiguration.class
//})
public class SimpleCacheManagerTests extends AbstractCachingManagerTests {

//    @Autowired
//    private CacheManager cacheManager;

//    @Autowired
//    private AddressService addressService;

    @BeforeEach
    public void setup() {
        this.initMemoryAppender(
                List.of(
                        Pair.with(SimpleCacheManagerTests.class.getPackageName(), Level.DEBUG)
                )
        );
        //
        super.setup_cacheManager();
    }

    @Test
    public void test_simpleCacheManager_basic() {
        log.debug("test_simpleCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_simpleCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);
        //
        super.test_xxxCacheManager_basic("ConcurrentMapCacheManager");
    }

    @Test
    public void test_getAddressModelById_basic() {
//        // given
//        String addressId = "address-01";
//        AddressModel addressModel01 = new AddressModel(
//                addressId
//                , "location-01"
//                , "street-01"
//                , "district-01"
//        );
//        this.addressService.saveAddressModel(addressModel01);
//
//        // when
//        AddressModel addressCache01 = this.addressService.getAddressModelById(addressId);
//        log.debug("test_getAddressModelById_basic - addressCache01: [{}]", addressCache01);
//        //
//        Cache cacheDefault = this.cacheManager.getCache("default");
//        Object object01 = cacheDefault == null ? null : cacheDefault.get(addressId);
//        log.debug("test_getAddressModelById_basic - object01: [{}]", object01);
//        //
//        AddressModel addressModel02 = new AddressModel(
//                addressId
//                , "location-02"
//                , "street-02"
//                , "district-02"
//        );
//        this.addressService.saveAddressModel(addressModel02);
//        AddressModel addressCache02 = this.addressService.getAddressModelById(addressId);
//        log.debug("test_getAddressModelById_basic - addressCache02: [{}]", addressCache02);
//        //
//        this.addressService.printAddressModelMap();
//        //
//        this.addressService.evictAllCacheValues();
//        //
//        AddressModel addressCache02b = this.addressService.getAddressModelById(addressId);
//        log.debug("test_getAddressModelById_basic - addressCache02b: [{}]", addressCache02b);
//        //
//        this.addressService.printAddressModelMap();
//
//        // then
//        // assertThat(actual).isEqualTo(expected)
//        assertThat(addressCache01.toString()).isEqualTo(addressCache02.toString());
//        assertThat(addressCache02.toString()).isEqualTo(addressModel01.toString());
////        assertThat(addressCache02b.toString()).isEqualTo(addressCache01.toString());
//        assertThat(addressCache02b.toString()).isEqualTo(addressModel02.toString());
        super.test_getAddressModelById_basic(true);
    }

    // No time to live for SimpleCacheManager
//    @Test
//    public void test_getAddressModelById_ttl_exceeded_1s() throws InterruptedException {
//        super.test_getAddressModelById_ttl_exceeded_1s(1000L);
//    }

}
