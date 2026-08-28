package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.aop.ReqVariableData;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckResult;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface RlacPermissionControlEvaluator {
    ResourceCheckResult evaluate(ResourceCheck resourceCheck
//                , String resourceId
//                , Map<String, String> validatedResources
                , ReqVariableData reqVariableData
                , Map<String, ResourceCheckResult> validatedResources
                , PermissionControl permissionControl
                , Authentication authentication
    );
}
