package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;

import java.util.Map;

public interface RlacPermissionControlEvaluator {
    boolean evaluate(ResourceCheck resourceCheck, Map<String, String> validatedResources);
}
