package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.aop.ReqVariableData;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface ResourceChecker {

    String getSupportedType();

    /**
     * @param resourceCheck      Check the current ResourceCheck
//     * @param resourceId         Resource Id
     * @param reqVariableData       Request path-var and param-var maps
     * @param validatedResources Validated resource Ids
     * @param permissionControl  PermissionControl annotation
     * @param authentication     Current authentication
     */
    ResourceCheckResult check(ResourceCheck resourceCheck
//            , String resourceId
//            , Map<String, String> validatedResources
            , ReqVariableData reqVariableData
            , Map<String, ResourceCheckResult> validatedResources
            , PermissionControl permissionControl
            , Authentication authentication
    );

}
