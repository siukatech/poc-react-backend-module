package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class ResourceCheckResult {
    private boolean hasAccess = false;
    private Map<String, String> outputMap = new HashMap<>();
    public ResourceCheckResult(boolean hasAccess) {
        this.setHasAccess(hasAccess);
    }
}
