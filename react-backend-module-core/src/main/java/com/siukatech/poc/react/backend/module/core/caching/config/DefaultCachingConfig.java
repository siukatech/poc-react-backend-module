package com.siukatech.poc.react.backend.module.core.caching.config;

import com.siukatech.poc.react.backend.module.core.caching.handler.CacheExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
//@EnableCaching  // This is not working.
public abstract class DefaultCachingConfig implements InitializingBean, CachingConfigurer {

    public static final String CACHE_NAME_DEFAULT = "default";
    public static final String CACHE_NAME_AUTH = "auth";
    public static final String CACHE_NAME_IDEMPOTENCY = "idempotency";

    /**
     * Override original "spring.cache.cache-names" set-up,
     * Add default, auth and idempotency to the cacheNameList
     */
    @Value("${spring.cache.cache-names:" + CACHE_NAME_DEFAULT + "}")
    private List<String> cacheNames;
    private List<String> cacheNameListWithDefaults;
    private final CacheExceptionHandler cacheExceptionHandler;

    protected DefaultCachingConfig(CacheExceptionHandler cacheExceptionHandler) {
        this.cacheExceptionHandler = cacheExceptionHandler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.cacheNameListWithDefaults = new ArrayList<>();
        this.cacheNameListWithDefaults.add(CACHE_NAME_DEFAULT);
        this.cacheNameListWithDefaults.add(CACHE_NAME_AUTH);
        this.cacheNameListWithDefaults.add(CACHE_NAME_IDEMPOTENCY);
        log.debug("afterPropertiesSet - this.cacheNames: [{}]", this.cacheNames);
        this.cacheNameListWithDefaults.addAll(
                this.cacheNames.stream()
                .filter(cn -> !cacheNameListWithDefaults.contains(cn)).collect(Collectors.toList())
        );
        log.debug("afterPropertiesSet - this.cacheNameListWithDefaults: [{}]", this.cacheNameListWithDefaults);
    }

    protected List<String> getCacheNameListWithDefaults() {
        return this.cacheNameListWithDefaults;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return this.cacheExceptionHandler;
    }
}
