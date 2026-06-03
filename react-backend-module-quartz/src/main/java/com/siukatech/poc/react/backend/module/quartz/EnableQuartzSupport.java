package com.siukatech.poc.react.backend.module.quartz;

import com.siukatech.poc.react.backend.module.core.EnableReactBackend;
import com.siukatech.poc.react.backend.module.quartz.config.QuartzSupportConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableReactBackend
@Import({
        QuartzSupportConfig.class
})
public @interface EnableQuartzSupport {
}
