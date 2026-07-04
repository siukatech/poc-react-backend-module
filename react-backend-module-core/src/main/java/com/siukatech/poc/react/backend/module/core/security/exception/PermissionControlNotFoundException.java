package com.siukatech.poc.react.backend.module.core.security.exception;

import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class PermissionControlNotFoundException extends RuntimeException {

    public PermissionControlNotFoundException(String msg) {
        super(msg);
    }

    private final static String TMPL_ACCESS_DENIED = "Access denied"
            + ", myAuthenticationToken.getAuthorities.size: [%s]"
            + ", myAuthenticationToken.getAuthorities.MyGrantedAuthority.count: [%s]"
            + ", userId: [%s]"
            + ", beanName: [%s], methodName: [%s]"
            + ", permissionControl: [%s]"
            + ", appResourceId: [%s], accessRight: [%s]"
            + ", authorityCount: [%d]"
            ;

    public static PermissionControlNotFoundException toPermissionControlNotFoundException(
            PermissionControlExceptionRec permissionControlExceptionRec) {
        return new PermissionControlNotFoundException(toExceptionMsg(permissionControlExceptionRec));
    }

    private static String toExceptionMsg(PermissionControlExceptionRec permissionControlExceptionRec) {
        Authentication authentication = permissionControlExceptionRec.authentication();
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>(authentication.getAuthorities());
        Integer grantedAuthorityListSize = grantedAuthorityList.size();
        Long grantedAuthorityListCount = grantedAuthorityList.stream()
                .filter(ga -> ga instanceof MyGrantedAuthority)
                .count();
        String userId = authentication.getName();
        String appResourceId = permissionControlExceptionRec.appResourceId();
        String accessRight = permissionControlExceptionRec.accessRight();
        Long authorityCount = grantedAuthorityList.stream()
                .filter(grantedAuthority -> grantedAuthority instanceof MyGrantedAuthority)
                .map(MyGrantedAuthority.class::cast)
                .peek(mga -> permissionControlExceptionRec.authorityCountPeek().accept(mga))
                .filter(mga -> mga.getAppResourceId() != null
                        && mga.getAppResourceId().equals(appResourceId)
                        && mga.getAccessRight() != null
                        && mga.getAccessRight().equals(accessRight)
                )
                .count();
        String accessDeniedMsg = String.format(TMPL_ACCESS_DENIED
                , grantedAuthorityListSize
                , grantedAuthorityListCount
                , userId
                , permissionControlExceptionRec.beanName()
                , permissionControlExceptionRec.methodName()
                , permissionControlExceptionRec.permissionControl()
                , permissionControlExceptionRec.appResourceId()
                , permissionControlExceptionRec.accessRight()
                , authorityCount
        );
        return accessDeniedMsg;
    }

}
