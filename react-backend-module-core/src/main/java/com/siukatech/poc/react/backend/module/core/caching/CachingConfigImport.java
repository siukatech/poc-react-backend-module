package com.siukatech.poc.react.backend.module.core.caching;

import com.siukatech.poc.react.backend.module.core.caching.config.*;
import com.siukatech.poc.react.backend.module.core.caching.handler.CacheExceptionHandler;
import org.springframework.context.annotation.Import;

@Import({
        CacheExceptionHandler.class
        , SimpleCachingConfig.class
        , EhcacheCachingConfig.class
        , RedisCachingConfig.class
        , CaffeineCachingConfig.class
        , CaffeineRedisCachingConfig.class
})
public class CachingConfigImport {
}
