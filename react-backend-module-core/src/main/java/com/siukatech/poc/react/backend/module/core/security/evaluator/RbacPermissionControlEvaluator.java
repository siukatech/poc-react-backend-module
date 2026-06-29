package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlNotFoundException;
import org.springframework.security.core.Authentication;

public interface RbacPermissionControlEvaluator {
    boolean evaluate(PermissionControl permissionControl, Authentication authentication)
            throws PermissionControlNotFoundException;
}
