package com.siukatech.poc.react.backend.module.core.security.resourcechecker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@AllArgsConstructor
@Setter
@Getter
public class ResourceCheckResult {
    private boolean hasAccess = false;
    private Map<String, String> resourceIdMap = new HashMap<>();
}
