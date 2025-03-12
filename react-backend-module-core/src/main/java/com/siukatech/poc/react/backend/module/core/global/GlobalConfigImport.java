package com.siukatech.poc.react.backend.module.core.global;

import com.siukatech.poc.react.backend.module.core.global.config.MapperConfig;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.global.config.PostAppConfig;
import org.springframework.context.annotation.Import;

@Import({AppCoreProp.class
        , MapperConfig.class
        , PostAppConfig.class
})
public class GlobalConfigImport {

}
