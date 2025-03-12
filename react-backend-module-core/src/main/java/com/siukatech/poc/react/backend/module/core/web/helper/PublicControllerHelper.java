package com.siukatech.poc.react.backend.module.core.web.helper;

import com.siukatech.poc.react.backend.module.core.web.annotation.base.PublicController;

public class PublicControllerHelper {

    public static String resolveExcludePath() {
        return "/v*" + PublicController.REQUEST_MAPPING_URI_PREFIX + "/**";
    }

}
