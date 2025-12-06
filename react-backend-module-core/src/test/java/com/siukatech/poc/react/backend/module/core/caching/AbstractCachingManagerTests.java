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

import java.time.Duration;
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

    protected abstract Duration getDefinedTtl();

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

    protected void test_getAddressModelById_basic(boolean hasAssertion) {
        this.test_getAddressModelById(false, this.getDefinedTtl().toMillis(), hasAssertion);
    }

    protected void test_getAddressModelById_ttl_exceeded_1s(long definedTtl, boolean hasAssertion) {
        this.test_getAddressModelById(true, definedTtl, hasAssertion);
    }

//    @Test
    private void test_getAddressModelById(boolean isTtlExceeded, long definedTtl, boolean hasAssertion) {
        // given
        String addressId = "address-01";
        AddressModel addressModel01 = new AddressModel(
                addressId
                , "location-01"
                , "street-01"
                , "district-01"
        );
        log.debug("test_getAddressModelById - saveAddressModel - 1, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
        this.addressService.saveAddressModel(addressModel01);
        log.debug("test_getAddressModelById - saveAddressModel - 1, isTtlExceeded: [{}], hasAssertion: [{}] - after, addressModel01: [{}]", isTtlExceeded, hasAssertion, addressModel01);

        // when
        log.debug("test_getAddressModelById - getAddressModelById - 1, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
        AddressModel addressCache01 = this.addressService.getAddressModelById(addressId, "1");
        log.debug("test_getAddressModelById - getAddressModelById - 1, isTtlExceeded: [{}], hasAssertion: [{}] - after, addressCache01: [{}]", isTtlExceeded, hasAssertion, addressCache01);
        //
        Cache cacheDefault = this.cacheManager.getCache("default");
        Object cachedObject01 = cacheDefault == null ? null : cacheDefault.get(addressId);
        log.debug("test_getAddressModelById - cacheDefault - 1, isTtlExceeded: [{}], hasAssertion: [{}] - cachedObject01: [{}]", isTtlExceeded, hasAssertion, cachedObject01);
        //
        AddressModel addressModel02 = new AddressModel(
                addressId
                , "location-02"
                , "street-02"
                , "district-02"
        );
        log.debug("test_getAddressModelById - saveAddressModel - 2, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
        this.addressService.saveAddressModel(addressModel02);
        log.debug("test_getAddressModelById - saveAddressModel - 2, isTtlExceeded: [{}], hasAssertion: [{}] - after, addressModel02: [{}]", isTtlExceeded, hasAssertion, addressModel02);
        //
        log.debug("test_getAddressModelById - getAddressModelById - 2, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
        AddressModel addressCache02 = this.addressService.getAddressModelById(addressId, "2");
        log.debug("test_getAddressModelById - getAddressModelById - 2, isTtlExceeded: [{}], hasAssertion: [{}] - after, addressCache02: [{}]", isTtlExceeded, hasAssertion, addressCache02);
        //
        log.debug("test_getAddressModelById - printAddressModelMap - 2, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
        this.addressService.printAddressModelMap();
        log.debug("test_getAddressModelById - printAddressModelMap - 2, isTtlExceeded: [{}], hasAssertion: [{}] - after", isTtlExceeded, hasAssertion);
        //
        if (!isTtlExceeded) {
            log.debug("test_getAddressModelById - evictAllCacheValues - 2, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
            this.addressService.evictAllCacheValues();
            log.debug("test_getAddressModelById - evictAllCacheValues - 2, isTtlExceeded: [{}], hasAssertion: [{}] - after", isTtlExceeded, hasAssertion);
        }
        else {
            try {
                log.debug("test_getAddressModelById - Thread.sleep - 2, isTtlExceeded: [{}], hasAssertion: [{}] - before - definedTtl: [{}]", isTtlExceeded, hasAssertion, definedTtl);
                Thread.sleep(definedTtl + 500);
                log.debug("test_getAddressModelById - Thread.sleep - 2, isTtlExceeded: [{}], hasAssertion: [{}] - after", isTtlExceeded, hasAssertion);
            }
            catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        //
        log.info("test_getAddressModelById - getAddressModelById - 3, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
        AddressModel addressCache02b = this.addressService.getAddressModelById(addressId, "3");
        log.info("test_getAddressModelById - getAddressModelById - 3, isTtlExceeded: [{}], hasAssertion: [{}] - after, addressCache02b: [{}]", isTtlExceeded, hasAssertion, addressCache02b);
        //
        log.info("test_getAddressModelById - printAddressModelMap - 3, isTtlExceeded: [{}], hasAssertion: [{}] - before", isTtlExceeded, hasAssertion);
        this.addressService.printAddressModelMap();
        log.debug("test_getAddressModelById - printAddressModelMap - 3, isTtlExceeded: [{}], hasAssertion: [{}] - after", isTtlExceeded, hasAssertion);

        // then
        if (hasAssertion) {
            // assertThat(actual).isEqualTo(expected)
            assertThat(addressCache01.toString()).isEqualTo(addressCache02.toString());
            assertThat(addressCache02.toString()).isEqualTo(addressModel01.toString());
            if (!isTtlExceeded) {
                // cache evicted
                // the addressCache02b resolved from addressService.getAddressModelById
                // no cache, value is same as database
                assertThat(addressCache02b.toString()).isEqualTo(addressModel02.toString());
            }
            else {
//                // cache existed - not expired
//                // the definedTtl should be different with the configuration like 'spring.cache.redis.time-to-live=2s'
//                // the addressCache02b resolved from addressService.getAddressModelById
//                // having cache, value is same as cache
//                assertThat(addressCache02b.toString()).isEqualTo(addressCache01.toString());
                //
                // actually cache should be expired
                // the addressCache02b resolved from addressService.getAddressModelById
                // no cache, value is same as database
                assertThat(addressCache02b.toString()).isEqualTo(addressModel02.toString());
            }
        }
    }

}
