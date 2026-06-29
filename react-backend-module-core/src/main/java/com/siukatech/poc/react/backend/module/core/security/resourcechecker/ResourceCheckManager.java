package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ResourceCheckManager {

    // Key: Resource Type (e.g., "SHOP"), Value: related Checker Bean
    private final Map<String, ResourceChecker> resourceCheckerMap = new ConcurrentHashMap<>();

    // Spring will automatically inject ResourceChecker Bean
    @Autowired
    public ResourceCheckManager(List<ResourceChecker> resourceCheckers) {
        for (ResourceChecker checker : resourceCheckers) {
            resourceCheckerMap.put(checker.getSupportedType().toUpperCase(), checker);
        }
    }

    public ResourceChecker getResourceChecker(String resourceType) {
        ResourceChecker resourceChecker = resourceCheckerMap.get(resourceType.toUpperCase());
        if (resourceChecker == null) {
            throw new IllegalArgumentException("No ResourceChecker registered for resourceType: [%s]".formatted(resourceType));
        }
        return resourceChecker;
    }
}
