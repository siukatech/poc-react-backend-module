package com.siukatech.poc.react.backend.module.user.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckResult;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class UserResourceChecker implements ResourceChecker {

    public ResourceCheckResult getUserInfo(String targetUserId, Authentication authentication) {
        log.info("getUserInfo - targetUserId: [{}], authentication.getName: [{}]"
                , targetUserId, authentication.getName());
        ResourceCheckResult resourceCheckResult = new ResourceCheckResult(true);
        resourceCheckResult.getOutputMap()
                .putAll(Map.of("authentication.getName", authentication.getName()))
        ;
        return resourceCheckResult;
    }

    public ResourceCheckResult getPublicKey(HttpHeaders httpHeaders, Authentication authentication) {
        log.trace("getPublicKey - httpHeaders: [{}]", httpHeaders);
        log.info("getPublicKey - httpHeaders.size: [{}], authentication.getName: [{}]"
                , httpHeaders.size(), authentication.getName());
        ResourceCheckResult resourceCheckResult = new ResourceCheckResult(true);
        resourceCheckResult.getOutputMap()
                .putAll(Map.of("authentication.getName", authentication.getName()))
        ;
        return resourceCheckResult;
    }

    public ResourceCheckResult getKeyInfo1(HttpHeaders httpHeaders, Authentication authentication) {
        log.trace("getKeyInfo1 - httpHeaders: [{}]", httpHeaders);
        log.info("getKeyInfo1 - httpHeaders.size: [{}], authentication.getName: [{}]"
                , httpHeaders.size(), authentication.getName());
        ResourceCheckResult resourceCheckResult = new ResourceCheckResult(true);
        resourceCheckResult.getOutputMap()
                .putAll(Map.of("authentication.getName", authentication.getName()))
        ;
        return resourceCheckResult;
    }

}
