package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface ResourceChecker {

    String getSupportedType();

    /**
     * @param resourceCheck       Check the current ResourceCheck
     * @param resourceId          Resource Id
     * @param validatedResources  Validated resource Ids
     * @param permissionControl   PermissionControl annotation
     * @param authentication      Current authentication
     */
    boolean check(ResourceCheck resourceCheck, String resourceId
            , Map<String, String> validatedResources
            , PermissionControl permissionControl
            , Authentication authentication);

}
