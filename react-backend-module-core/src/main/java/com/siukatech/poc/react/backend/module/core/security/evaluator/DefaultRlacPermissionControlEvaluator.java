package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckManager;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceChecker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@AllArgsConstructor
//@Component
//@ConditionalOnMissingBean(value = {RlacEvaluator.class})
public class DefaultRlacPermissionControlEvaluator implements RlacPermissionControlEvaluator {

    private final ResourceCheckManager resourceCheckManager;

    public boolean evaluate(ResourceCheck resourceCheck, Map<String, String> validatedResources) {
        // Get ResourceChecker through ResourceCheckManager
        String resourceType = resourceCheck.resourceType();
        String accessRight = resourceCheck.accessRight();
        ResourceChecker resourceChecker = resourceCheckManager.getResourceChecker(resourceType);
        boolean hasAccess = resourceChecker.check(resourceCheck, validatedResources);
        log.debug("evaluate - resourceType: [{}], accessRight: [{}], hasAccess: [{}], resourceChecker: [{}]"
                , resourceType, accessRight, hasAccess, resourceChecker);
        return hasAccess;
    }

}
