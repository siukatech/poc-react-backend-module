package com.siukatech.poc.react.backend.module.core.web.annotation.base;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
@RestController
//@PublicController
//@ApiV1Controller
//@RequestMapping(PublicNoUriPrefixApiV1Controller.REQUEST_MAPPING_URI_PREFIX)
public @interface RestApiController {

}
