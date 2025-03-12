package com.siukatech.poc.react.backend.module.core.security.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

@Slf4j
public class AuthorizationDataCacheKeyGenerator implements KeyGenerator {

    public static final String CACHE_KEY_findUserByUserIdAndTokenValue
            = "AuthorizationDataProvider.findUserByUserIdAndTokenValue_";
    public static final String CACHE_KEY_findPermissionsByUserIdAndTokenValue
            = "AuthorizationDataProvider.findPermissionsByUserIdAndTokenValue_";
    public static final String CACHE_KEY_findDossierByUserIdAndTokenValue
            = "AuthorizationDataProvider.findDossierByUserIdAndTokenValue_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        log.debug("generate - target: [{}], method: [{}], params: [{}]", target, method, params);
        String userId = (String) params[0];
        String key = CACHE_KEY_findDossierByUserIdAndTokenValue + userId;
        log.debug("generate - target: [{}], userId: [{}], key: [{}]", target, userId, key);
        return key;
    }

}
