package com.siukatech.poc.react.backend.module.core.security.controller;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.web.annotation.v1.ProtectedApiV1Controller;
import org.springframework.web.bind.annotation.GetMapping;

@ProtectedApiV1Controller
public class ProtectedUrlController {

    @GetMapping(path = "/protected-url/authorized")
    @PermissionControl(appResourceId = "core.protectedUrl.authorized", accessRight = "view")
    public String authorized() {
        return "authorized";
    }

    @GetMapping(path = "/protected-url/access_denied")
    @PermissionControl(appResourceId = "core.protectedUrl.accessDenied", accessRight = "view")
    public String accessDenied() {
        return "authorized";
    }

}
