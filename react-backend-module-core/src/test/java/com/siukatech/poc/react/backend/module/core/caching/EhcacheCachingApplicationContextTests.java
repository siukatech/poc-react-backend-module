package com.siukatech.poc.react.backend.module.core.caching;


import ch.qos.logback.classic.Level;
import com.siukatech.poc.react.backend.module.core.caching.config.EhcacheCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.model.AddressModel;
import com.siukatech.poc.react.backend.module.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ApplicationContext;
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
@SpringJUnitConfig(value = EhcacheCachingApplicationContextTests.EhcacheCachingManagerConfig.class)
//@ImportAutoConfiguration(classes = {
//        CacheAutoConfiguration.class
//})
//public class EhcacheCachingManagerTests extends AbstractUnitTests {
public class EhcacheCachingApplicationContextTests extends AbstractCachingManagerTests {

    @Autowired
    private ApplicationContext applicationContext;

    @EnableAspectJAutoProxy(proxyTargetClass = false)
    @TestComponent
    public static class EhcacheCachingManagerConfig {

    }

    @BeforeEach
    public void setup() {
        this.initMemoryAppender(
                List.of(
                        Pair.with(EhcacheCachingApplicationContextTests.class.getPackageName(), Level.DEBUG)
                )
        );
    }

    @BeforeEach
    public void setup2() {
        log.debug("setup2");
        super.setup_cacheManager();
    }

    @Test
    public void test_applicationContext_getBean() {
//        AddressService addressServiceFromBean = (AddressService) applicationContext.getBean("addressService");
        AddressService addressServiceFromBean = applicationContext.getBean(AddressService.class);
        String addressId = "address-01";
//        AddressModel addressModel1 = addressServiceFromBean.getAddressModelById(addressId, "999");
        super.test_getAddressModelById_basic(false);
        AddressModel addressModel2 = addressServiceFromBean.getAddressModelById(addressId, "999");
//        log.debug("test_applicationContext_getBean - addressModel1: [{}]", addressModel1);
//        log.debug("test_applicationContext_getBean - addressModel2: [{}]", addressModel2);
    }

}
