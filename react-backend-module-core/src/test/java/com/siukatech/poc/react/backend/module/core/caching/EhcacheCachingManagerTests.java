package com.siukatech.poc.react.backend.module.core.caching;


import ch.qos.logback.classic.Level;
import com.siukatech.poc.react.backend.module.core.caching.config.EhcacheCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.model.AddressModel;
import com.siukatech.poc.react.backend.module.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@Slf4j
@SpringBootTest(classes = {EhcacheCachingConfig.class
        , AddressService.class
    }
    , properties = {
        "spring.cache.type=ehcache"
        , "spring.cache.cache-names=test1,test2"
        , "spring.cache.ehcache.time-to-live=1s"
        , "logging.level.root=INFO"
        , "logging.level.com.siukatech.poc.react.backend.module.core.caching=DEBUG"
    }
)
//@ImportAutoConfiguration(classes = {
//        CacheAutoConfiguration.class
//})
//public class EhcacheCachingManagerTests extends AbstractUnitTests {
public class EhcacheCachingManagerTests extends AbstractCachingManagerTests {

//    @Autowired
//    private CacheManager cacheManager;

//    @Autowired
//    private AddressService addressService;

    @BeforeEach
    public void setup() {
        this.initMemoryAppender(
                List.of(
                        Pair.with(EhcacheCachingManagerTests.class.getPackageName(), Level.DEBUG)
                )
        );
    }

    @BeforeEach
    public void setup2() {
        log.debug("setup2");
        super.setup_cacheManager();
    }

    @AfterAll
    public static void teardown() {

    }

    @Test
    public void test_ehcacheCacheManager_basic() {
//        log.debug("test_ehcacheCacheManager_basic - getCacheNames: [{}]"
//                , this.cacheManager.getCacheNames());
//        log.debug("test_ehcacheCacheManager_basic - cacheManager: [{}]"
//                , this.cacheManager);
//
//        List<ILoggingEvent> loggingEventList = this.getMemoryAppender().search("JCacheCacheManager", Level.DEBUG);
//        Assertions.assertTrue((!loggingEventList.isEmpty()), "JCacheCacheManager not found");
        super.test_xxxCacheManager_basic("JCacheCacheManager");
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

    @Test
    public void test_getAddressModelById_ttl_exceeded_1s() throws InterruptedException {
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
//        log.debug("test_getAddressModelById_ttl_exceeded_1s - addressCache01: [{}]", addressCache01);
//        //
//        Cache cacheDefault = this.cacheManager.getCache("default");
//        Object object01 = cacheDefault == null ? null : cacheDefault.get(addressId);
//        log.debug("test_getAddressModelById_ttl_exceeded_1s - object01: [{}]", object01);
//        //
//        AddressModel addressModel02 = new AddressModel(
//                addressId
//                , "location-02"
//                , "street-02"
//                , "district-02"
//        );
//        this.addressService.saveAddressModel(addressModel02);
//        AddressModel addressCache02 = this.addressService.getAddressModelById(addressId);
//        log.debug("test_getAddressModelById_ttl_exceeded_1s - addressCache02: [{}]", addressCache02);
//        //
//        this.addressService.printAddressModelMap();
//        //
//        //this.addressService.evictAllCacheValues();
//        Thread.sleep(2500L);
//        //
//        AddressModel addressCache02b = this.addressService.getAddressModelById(addressId);
//        log.debug("test_getAddressModelById_ttl_exceeded_1s - addressCache02b: [{}]", addressCache02b);
//        //
//        this.addressService.printAddressModelMap();
//
//        // then
//        // assertThat(actual).isEqualTo(expected)
//        assertThat(addressCache01.toString()).isEqualTo(addressCache02.toString());
//        assertThat(addressCache02.toString()).isEqualTo(addressModel01.toString());
//        assertThat(addressCache02b.toString()).isEqualTo(addressModel02.toString());
        super.test_getAddressModelById_ttl_exceeded_1s(1000L, true);
    }

}
