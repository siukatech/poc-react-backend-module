package com.siukatech.poc.react.backend.module.user;

import com.siukatech.poc.react.backend.module.core.EnableReactBackend;
import com.siukatech.poc.react.backend.module.user.config.UserSupportConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableReactBackend
@Import(value = {
        UserSupportConfig.class
})
public @interface EnableUserSupport {
}
