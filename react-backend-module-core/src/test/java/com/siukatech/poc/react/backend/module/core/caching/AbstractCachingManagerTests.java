package com.siukatech.poc.react.backend.module.core.caching;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.caching.model.AddressModel;
import com.siukatech.poc.react.backend.module.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public abstract class AbstractCachingManagerTests extends AbstractUnitTests {

//    @SpyBean
//    protected ObjectMapper objectMapper;

    @Autowired
    protected CacheManager cacheManager;

    @Autowired
    protected AddressService addressService;

    protected void setup_cacheManager() {
        this.addressService.evictAllCacheValues();
    }

    protected void test_xxxCacheManager_basic(String cacheManagerName) {
        log.debug("test_xxxCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_xxxCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);

        List<ILoggingEvent> loggingEventList = this.getMemoryAppender().search(cacheManagerName, Level.DEBUG);
        Assertions.assertTrue((!loggingEventList.isEmpty()), "%s not found".formatted(cacheManagerName));
    }

    protected void test_getAddressModelById_basic() {
        this.test_getAddressModelById(false, 1000L);
    }

    protected void test_getAddressModelById_ttl_exceeded_1s(long definedTtl) {
        this.test_getAddressModelById(true, definedTtl);
    }

//    @Test
    private void test_getAddressModelById(boolean isTtlExceeded, long definedTtl) {
        // given
        String addressId = "address-01";
        AddressModel addressModel01 = new AddressModel(
                addressId
                , "location-01"
                , "street-01"
                , "district-01"
        );
        log.debug("test_getAddressModelById - saveAddressModel - before");
        this.addressService.saveAddressModel(addressModel01);
        log.debug("test_getAddressModelById - saveAddressModel - after, addressModel01: [{}]", addressModel01);

        // when
        log.debug("test_getAddressModelById - getAddressModelById - before");
        AddressModel addressCache01 = this.addressService.getAddressModelById(addressId);
        log.debug("test_getAddressModelById - getAddressModelById - after, addressCache01: [{}]", addressCache01);
        //
        Cache cacheDefault = this.cacheManager.getCache("default");
        Object cachedObject01 = cacheDefault == null ? null : cacheDefault.get(addressId);
        log.debug("test_getAddressModelById - cachedObject01: [{}]", cachedObject01);
        //
        AddressModel addressModel02 = new AddressModel(
                addressId
                , "location-02"
                , "street-02"
                , "district-02"
        );
        log.debug("test_getAddressModelById - saveAddressModel - before");
        this.addressService.saveAddressModel(addressModel02);
        log.debug("test_getAddressModelById - saveAddressModel - after, addressModel02: [{}]", addressModel02);
        //
        log.debug("test_getAddressModelById - getAddressModelById - before");
        AddressModel addressCache02 = this.addressService.getAddressModelById(addressId);
        log.debug("test_getAddressModelById - getAddressModelById - after, addressCache02: [{}]", addressCache02);
        //
        log.debug("test_getAddressModelById - printAddressModelMap - before");
        this.addressService.printAddressModelMap();
        log.debug("test_getAddressModelById - printAddressModelMap - after");
        //
        if (!isTtlExceeded) {
            log.debug("test_getAddressModelById - evictAllCacheValues - before");
            this.addressService.evictAllCacheValues();
            log.debug("test_getAddressModelById - evictAllCacheValues - after");
        }
        else {
            try {
                log.debug("test_getAddressModelById - Thread.sleep - before - definedTtl: [{}]", definedTtl);
                Thread.sleep(definedTtl + 500);
                log.debug("test_getAddressModelById - Thread.sleep - after");
            }
            catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        //
        log.info("test_getAddressModelById - getAddressModelById - before");
        AddressModel addressCache02b = this.addressService.getAddressModelById(addressId);
        log.info("test_getAddressModelById - getAddressModelById - after, addressCache02b: [{}]", addressCache02b);
        //
        log.info("test_getAddressModelById - printAddressModelMap - before");
        this.addressService.printAddressModelMap();
        log.debug("test_getAddressModelById - printAddressModelMap - after");

        // then
        // assertThat(actual).isEqualTo(expected)
        assertThat(addressCache01.toString()).isEqualTo(addressCache02.toString());
        assertThat(addressCache02.toString()).isEqualTo(addressModel01.toString());
//        assertThat(addressCache02b.toString()).isEqualTo(addressCache01.toString());
        assertThat(addressCache02b.toString()).isEqualTo(addressModel02.toString());
    }

}
