package com.siukatech.poc.react.backend.module.core.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
// https://www.concretepage.com/spring/spring-component-scan-include-and-exclude-filter-example
@ComponentScan(value = {
        "com.siukatech.poc.react.backend.module.core.web"
        , "com.siukatech.poc.react.backend.module.core.data"
        , "com.siukatech.poc.react.backend.module.core.business"
}
        , excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX
//          , classes = IUserService.class
//          , pattern = "com.concretepage.*.*Util"
        , pattern = "com.siukatech.poc.react.backend.module.core.web.controller.*"
))
//@Import({
//        GlobalExceptionHandler.class
//        , CryptoContext.class
//        , EncryptedRequestBodyAdvice.class
//        , EncryptedResponseBodyAdvice.class
//})
public class WebComponentConfig {
//
//    /**
//     * Reference:
//     * https://stackoverflow.com/a/67973800
//     * https://github.com/FasterXML/jackson-databind/issues/3262#issuecomment-909008472
//     *
//     * @return
//     */
//    @Bean
//    public ObjectMapper objectMapper() {
////        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
////                .json()
////                .build();
////        objectMapper().registerModule(new JavaTimeModule());
//
//        ObjectMapper objectMapper = JsonMapper.builder()
//                .addModule(new JavaTimeModule())
//
//                .serializationInclusion(JsonInclude.Include.NON_NULL)
//                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
////                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
//
//                // here is configured for encrypted data in EncryptedRequestBodyAdvice and EncryptedResponseBodyAdvice
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//
//                // ignore unknown json properties to prevent HttpMessageNotReadableException
//                // https://stackoverflow.com/a/5455563
////                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
//                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
////                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//
//                .build();
//
////        MyObjectMapper objectMapper = new MyObjectMapper();
//
//        return objectMapper;
//    }
//
//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }

}
