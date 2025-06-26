package com.siukatech.poc.react.backend.module.core.web.context;

import com.siukatech.poc.react.backend.module.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.module.core.business.form.encrypted.EncryptedDetail;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Reference:
 * xxx - https://shzhangji.com/blog/2022/07/05/store-custom-data-in-spring-mvc-request-context/
 * https://github.com/amalpotra/context-holder
 * https://www.javabyexamples.com/request-scoped-data-with-spring-mvc
 */
@Data
@Component
@RequestScope
public class EncryptedBodyContext {

    public static final String CONTEXT_NAME = "ENCRYPTED_BODY_CONTEXT";

    //    private UserEntity userEntity;
    private MyKeyDto myKeyDto;
    private EncryptedDetail encryptedDetail;
    private String objectId;

}
