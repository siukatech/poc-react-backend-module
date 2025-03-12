package com.siukatech.poc.react.backend.module.core.caching;

import com.siukatech.poc.react.backend.module.core.caching.config.EhcacheCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.config.SimpleCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.config.RedisCachingConfig;
import org.springframework.context.annotation.Import;

@Import({
        SimpleCachingConfig.class
        , EhcacheCachingConfig.class
        , RedisCachingConfig.class
})
public class CachingConfigImport {
}
