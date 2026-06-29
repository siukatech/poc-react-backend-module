package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.constant.SecurityConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnMissingBean(name = "applicationResourceChecker") // Or specified by type if it's the only one
public class ApplicationResourceChecker implements ResourceChecker {

    @Override
    public String getSupportedType() {
        return SecurityConstants.ResourceType.APPLICATION;
    }

    @Override
    public boolean check(ResourceCheck resourceCheck, Map<String, String> validatedResources) {
        return true;
    }
}
