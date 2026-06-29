package com.siukatech.poc.react.backend.module.user.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceChecker;
import com.siukatech.poc.react.backend.module.user.security.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UserResourceChecker implements ResourceChecker {

    @Override
    public String getSupportedType() {
        // Use Constant
        return SecurityConstants.ResourceType.USER;
    }

    @Override
    public boolean check(ResourceCheck resourceCheck, Map<String, String> validatedResources) {
        // Get the previous resource id from Context
//        String parentShopId = validatedResources.get(SecurityConstants.ResourceType.APPLICATION);

        // Business logic...
        return true;
    }
}
