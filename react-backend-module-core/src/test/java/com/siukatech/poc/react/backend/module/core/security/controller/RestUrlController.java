package com.siukatech.poc.react.backend.module.core.security.controller;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.web.annotation.base.RestApiController;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
public class RestUrlController {

    @GetMapping(path = "/rest-url/authorized")
    @PermissionControl(appResourceId = "core.restUrl.authorized", accessRight = "view")
    public String authorized() {
        return "authorized";
    }

    @GetMapping(path = "/rest-url/access_denied")
    @PermissionControl(appResourceId = "core.restUrl.accessDenied", accessRight = "view")
    public String accessDenied() {
        return "authorized";
    }

}
