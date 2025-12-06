package com.siukatech.poc.react.backend.module.core.caching;

import com.siukatech.poc.react.backend.module.core.caching.config.*;
import com.siukatech.poc.react.backend.module.core.caching.handler.DefaultCacheErrorHandler;
import org.springframework.context.annotation.Import;

@Import({
        DefaultCacheErrorHandler.class
        , SimpleCachingConfig.class
        , EhcacheCachingConfig.class
        , RedisCachingConfig.class
        , CaffeineCachingConfig.class
        , RedisCaffeineCachingConfig.class
})
public class CachingConfigImport {
}
