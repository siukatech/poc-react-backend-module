package com.siukatech.poc.react.backend.module.core.web.annotation.v1;


import com.siukatech.poc.react.backend.module.core.web.annotation.base.ProtectedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
@RestController
@ProtectedController
@ApiV1Controller
@RequestMapping(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX)
public @interface ProtectedApiV1Controller {

    String REQUEST_MAPPING_URI_PREFIX = ApiV1Controller.REQUEST_MAPPING_API_VERSION + ProtectedController.REQUEST_MAPPING_URI_PREFIX;

}
