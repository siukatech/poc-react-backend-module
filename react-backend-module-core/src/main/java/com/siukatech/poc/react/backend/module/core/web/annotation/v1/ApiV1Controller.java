package com.siukatech.poc.react.backend.module.core.web.annotation.v1;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiV1Controller {
    String REQUEST_MAPPING_API_VERSION = "/v1";
}
