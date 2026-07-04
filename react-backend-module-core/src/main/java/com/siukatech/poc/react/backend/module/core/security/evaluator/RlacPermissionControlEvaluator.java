package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface RlacPermissionControlEvaluator {
    boolean evaluate(ResourceCheck resourceCheck
            , String resourceId
            , Map<String, String> validatedResources
            , PermissionControl permissionControl
            , Authentication authentication
    );
}
