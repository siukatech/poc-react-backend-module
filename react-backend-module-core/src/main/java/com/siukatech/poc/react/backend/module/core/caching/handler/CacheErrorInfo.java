package com.siukatech.poc.react.backend.module.core.caching.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CacheErrorInfo {
    private boolean isRedisTimeoutOrConnectionError;
    private String exceptionName;
}
