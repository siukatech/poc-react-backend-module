package com.siukatech.poc.react.backend.module.core.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

@Slf4j
public class AuthorizationDataDossierCacheKeyGenerator implements KeyGenerator {

    public static final String CACHE_KEY_findDossierByUserIdAndTokenValue
            = "AuthorizationDataProvider.findDossierByUserIdAndTokenValue_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        log.debug("generate - target: [{}], method: [{}], params: [{}]", target, method, params);
        try {
            String userId = (String) params[0];
            String key = (CACHE_KEY_findDossierByUserIdAndTokenValue + "%s").formatted(userId);
            log.debug("generate - target: [{}], userId: [{}], key: [{}]", target, userId, key);
            return key;
        } catch (Exception e) {
            String message = "generate - params does not contain userId";
            log.error(message);
            throw new IllegalArgumentException(message, e);
        }
    }

}
