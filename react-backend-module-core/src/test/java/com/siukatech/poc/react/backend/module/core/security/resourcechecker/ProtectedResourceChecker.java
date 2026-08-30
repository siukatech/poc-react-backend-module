package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProtectedResourceChecker implements ResourceChecker {
    public ResourceCheckResult noArg() {
        ResourceCheckResult resourceCheckResult = new ResourceCheckResult(true);
        log.info("getKeyInfo1 - noArg!!");
        return resourceCheckResult;
    }
}
