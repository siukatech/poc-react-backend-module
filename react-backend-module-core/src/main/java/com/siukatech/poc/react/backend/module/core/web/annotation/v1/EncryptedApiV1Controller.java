package com.siukatech.poc.react.backend.module.core.web.annotation.v1;


import com.siukatech.poc.react.backend.module.core.web.annotation.base.EncryptedController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
@RestController
@EncryptedController
@ApiV1Controller
@RequestMapping(EncryptedApiV1Controller.REQUEST_MAPPING_URI_PREFIX)
public @interface EncryptedApiV1Controller {

    String REQUEST_MAPPING_URI_PREFIX = ApiV1Controller.REQUEST_MAPPING_API_VERSION + EncryptedController.REQUEST_MAPPING_URI_PREFIX;

}


