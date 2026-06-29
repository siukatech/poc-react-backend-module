package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;

import java.util.Map;

public interface ResourceChecker {

    String getSupportedType();

    /**
     * @param resourceCheck       Check the current ResourceCheck
     * @param validatedResources  Validated resource Ids
     */
    boolean check(ResourceCheck resourceCheck, Map<String, String> validatedResources);

}
