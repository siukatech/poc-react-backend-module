package com.siukatech.poc.react.backend.module.core.security.exception;

public record PermissionControlExceptionRec(
        Integer grantedAuthorityListSize
        , Long grantedAuthorityListCount
        , String userId, String beanName, String methodName
        , String permissionControl
        , String appResourceId, String accessRight
        , Long authorityCount
) {
}
