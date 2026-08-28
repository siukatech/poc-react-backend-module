package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.aop.ReqVariableData;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckManager;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckResult;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceChecker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import java.util.Map;

@Slf4j
@AllArgsConstructor
//@Component
//@ConditionalOnMissingBean(value = {RlacEvaluator.class})
public class DefaultRlacPermissionControlEvaluator implements RlacPermissionControlEvaluator {

    private final ResourceCheckManager resourceCheckManager;

    public ResourceCheckResult evaluate(ResourceCheck resourceCheck
//            , String resourceId
//            , Map<String, String> validatedResources
            , ReqVariableData reqVariableData
            , Map<String, ResourceCheckResult> validatedResources
            , PermissionControl permissionControl
            , Authentication authentication
    ) {
        // Get ResourceChecker through ResourceCheckManager
        String resourceType = resourceCheck.resourceType();
        String accessRight = resourceCheck.accessRight();
        ResourceChecker resourceChecker = resourceCheckManager.getResourceChecker(resourceType);
        ResourceCheckResult resourceCheckResult = resourceChecker.check(
                resourceCheck
//                , resourceId
                , reqVariableData
                , validatedResources
                , permissionControl
                , authentication
        );
        boolean hasAccess = resourceCheckResult.isHasAccess();
        log.debug("evaluate - resourceType: [{}]"
//                        + ", resourceId: [{}]"
                        + ", reqVariableData: [{}]"
                        + ", accessRight: [{}], hasAccess: [{}], resourceChecker: [{}]"
                , resourceType
//                , resourceId
                , reqVariableData
                , accessRight
                , hasAccess
                , resourceChecker
        );
        return resourceCheckResult;
    }

}
