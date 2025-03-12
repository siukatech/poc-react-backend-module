package com.siukatech.poc.react.backend.module.core;


import com.siukatech.poc.react.backend.module.core.caching.CachingConfigImport;
import com.siukatech.poc.react.backend.module.core.data.DataConfigImport;
import com.siukatech.poc.react.backend.module.core.global.GlobalConfigImport;
import com.siukatech.poc.react.backend.module.core.security.SecurityConfigImport;
import com.siukatech.poc.react.backend.module.core.web.WebConfigImport;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        GlobalConfigImport.class
        , CachingConfigImport.class
        , DataConfigImport.class
        , WebConfigImport.class
        , SecurityConfigImport.class
})
public @interface EnableReactBackend {
}
