package com.siukatech.poc.react.backend.module.core.security.exception;

public class PermissionControlNotFoundException extends RuntimeException {
    public PermissionControlNotFoundException(String msg) {
        super(msg);
    }
}
