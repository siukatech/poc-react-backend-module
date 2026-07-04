package com.siukatech.poc.react.backend.module.user.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceChecker;
import com.siukatech.poc.react.backend.module.user.security.constant.UserSecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UserResourceChecker implements ResourceChecker {

    @Override
    public String getSupportedType() {
        // Use Constant
        return UserSecurityConstants.ResourceType.USER;
    }

    @Override
    public boolean check(ResourceCheck resourceCheck
            , String resourceId
            , Map<String, String> validatedResources
            , PermissionControl permissionControl
            , Authentication authentication) {
        // Get the previous resource id from Context
//        String parentShopId = validatedResources.get(UserSecurityConstants.ResourceType.APPLICATION);

        boolean hasAccess = false;
        hasAccess = true;
        log.info("check - resourceCheck: [{}]"
                        + ", resourceId: [{}]"
                        + ", validatedResources: [{}]"
                        + ", permissionControl: [{}]"
//                        + ", authentication: [{}]"
                        + ", hasAccess: [{}]"
                        + ", start"
                , resourceCheck, resourceId, validatedResources, permissionControl
//                , authentication
                , hasAccess
        );
        return hasAccess;
    }
}
