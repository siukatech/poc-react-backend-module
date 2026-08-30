package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Component
public class ApplicationResourceChecker implements ResourceChecker {

    public ResourceCheckResult getPermissionInfoAndUserDossier(
            HttpHeaders httpHeaders
            , String applicationId
            , Authentication authentication) {
        log.trace("getPermissionInfoAndUserDossier - httpHeaders: [{}]", httpHeaders);
        log.info("getPermissionInfoAndUserDossier - httpHeaders.size: [{}]"
                        + ", applicationId: [{}]"
                        + ", authentication.getName: [{}]"
                , httpHeaders.size(), applicationId, authentication.getName()
        );
        ResourceCheckResult resourceCheckResult = new ResourceCheckResult(true);
        resourceCheckResult.getOutputMap()
                .putAll(Map.of("applicationId", Objects.nonNull(applicationId)?applicationId:"NULL"))
        ;
        return resourceCheckResult;
    }

}
