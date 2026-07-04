package com.siukatech.poc.react.backend.module.core.security.exception;

import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import org.springframework.security.core.Authentication;

import java.util.function.Consumer;

public record PermissionControlExceptionRec(
        Authentication authentication
//        , Integer grantedAuthorityListSize
//        , Long grantedAuthorityListCount
//        , String userId
        , String beanName, String methodName
        , String permissionControl
        , String appResourceId, String accessRight
//        , Long authorityCount
        , Consumer<MyGrantedAuthority> authorityCountPeek
) {
}
