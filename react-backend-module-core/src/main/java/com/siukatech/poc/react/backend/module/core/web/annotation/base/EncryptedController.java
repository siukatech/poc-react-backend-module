package com.siukatech.poc.react.backend.module.core.web.annotation.base;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptedController {
    String REQUEST_MAPPING_URI_PREFIX = "/encrypted";
}
