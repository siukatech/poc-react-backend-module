package com.siukatech.poc.react.backend.module.core.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

@Slf4j
public class AuthorizationDataPermissionCacheKeyGenerator implements KeyGenerator {

    public static final String CACHE_KEY_findPermissionsByUserIdAndTokenValue
            = "AuthorizationDataProvider.findPermissionsByUserIdAndTokenValue_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        log.debug("generate - target: [{}], method: [{}], params: [{}]", target, method, params);
        try {
            String userId = (String) params[0];
            String tokenValue = (String) params[1];
            String key = (CACHE_KEY_findPermissionsByUserIdAndTokenValue + "%s_%s").formatted(userId, tokenValue);
            log.debug("generate - target: [{}], userId: [{}], tokenValue: [{}], key: [{}]", target, userId, tokenValue, key);
            return key;
        } catch (Exception e) {
            String message = "generate - params does not contain userId, tokenValue";
            log.error(message);
            throw new IllegalArgumentException(message, e);
        }
    }

}
