package com.siukatech.poc.react.backend.module.core.security.exception;

public class PermissionControlNotFoundException extends RuntimeException {

    public PermissionControlNotFoundException(String msg) {
        super(msg);
    }

    private final static String TMPL_ACCESS_DENIED = "Access denied"
            + ", myAuthenticationToken.getAuthorities.size: [%s]"
            + ", myAuthenticationToken.getAuthorities.MyGrantedAuthority.count: [%s]"
            + ", userId: [%s]"
//            + ", beanName: [%s], methodName: [%s]"
            + ", permissionControl: [%s]"
            + ", appResourceId: [%s], accessRight: [%s]"
            + ", authorityCount: [%d]";

    public static PermissionControlNotFoundException toPermissionControlNotFoundException(
            PermissionControlExceptionRec permissionControlExceptionRec) {
        return new PermissionControlNotFoundException(toExceptionMsg(permissionControlExceptionRec));
    }

    private static String toExceptionMsg(PermissionControlExceptionRec permissionControlExceptionRec) {
        String accessDeniedMsg = String.format(TMPL_ACCESS_DENIED
                , permissionControlExceptionRec.grantedAuthorityListSize()
                , permissionControlExceptionRec.grantedAuthorityListCount()
                , permissionControlExceptionRec.userId()
//                , permissionControlExceptionRec.beanName()
//                , permissionControlExceptionRec.methodName()
                , permissionControlExceptionRec.permissionControl()
                , permissionControlExceptionRec.appResourceId()
                , permissionControlExceptionRec.accessRight()
                , permissionControlExceptionRec.authorityCount()
        );
        return accessDeniedMsg;
    }

}
