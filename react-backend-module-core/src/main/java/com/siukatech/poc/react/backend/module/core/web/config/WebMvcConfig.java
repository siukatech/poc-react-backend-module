package com.siukatech.poc.react.backend.module.core.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.siukatech.poc.react.backend.module.core.security.interceptor.AuthorizationDataInterceptor;
import com.siukatech.poc.react.backend.module.core.security.interceptor.PermissionControlInterceptor;
import com.siukatech.poc.react.backend.module.core.web.helper.PublicControllerHelper;
import com.siukatech.poc.react.backend.module.core.web.interceptor.CorrelationIdInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
//public class WebMvcConfig extends WebMvcConfigurationSupport {
public class WebMvcConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final AuthorizationDataInterceptor authorizationDataInterceptor;
    private final PermissionControlInterceptor permissionControlInterceptor;
    private final CorrelationIdInterceptor correlationIdInterceptor;

    public WebMvcConfig(ObjectMapper objectMapper
            , AuthorizationDataInterceptor authorizationDataInterceptor
            , PermissionControlInterceptor permissionControlInterceptor
            , CorrelationIdInterceptor correlationIdInterceptor
    ) {
        this.objectMapper = objectMapper;
        this.authorizationDataInterceptor = authorizationDataInterceptor;
        this.permissionControlInterceptor = permissionControlInterceptor;
        this.correlationIdInterceptor = correlationIdInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.debug("addCorsMappings - start");
        registry
                .addMapping("/**")
//                .allowedMethods(HttpMethod.HEAD.name()
//                        , HttpMethod.GET.name()
//                        , HttpMethod.POST.name()
//                        , HttpMethod.PUT.name()
//                        , HttpMethod.DELETE.name()
//                        , HttpMethod.PATCH.name()
//                        , HttpMethod.OPTIONS.name()
//                )
                .allowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).toArray(String[]::new))
                //.allowedOrigins("http://localhost:3000/")
                .allowedOrigins("*")
        ;
        log.debug("addCorsMappings - end");
    }

    /**
     * Reference:
     * https://baeldung.com/spring-boot-formatting-json-dates
     */
    @Bean
    @ConditionalOnProperty(value = "spring.jackson.date-format", matchIfMissing = true, havingValue = "none")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            log.debug("jackson2ObjectMapperBuilderCustomizer - start");
            String dateFormat = "yyyy-MM-dd";
            String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

            builder.simpleDateFormat(dateTimeFormat);
            // deserializers
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));

            log.debug("jackson2ObjectMapperBuilderCustomizer - end");
        };
    }

    /**
     * Reference:
     * https://stackoverflow.com/q/36906877
     * disable feature WRITE_DATES_AS_TIMESTAMPS
     *
     * @param converters the list of configured converters to extend
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.debug("extendMessageConverters - start");
        log.debug("extendMessageConverters - converters.size: [{}]", converters.size());
        converters.stream().forEach(httpMessageConverter -> {
            log.debug("extendMessageConverters - httpMessageConverter.getClass.getName: [{}]", httpMessageConverter.getClass().getName());
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
////                MappingJackson2HttpMessageConverter jacksonMessageConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
//                ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
//                log.debug("extendMessageConverters - MappingJackson2HttpMessageConverter.getObjectMapper: [{}]"
//                        , mappingJackson2HttpMessageConverter.getObjectMapper());
//                objectMapper =
//                        // here is configured for non-encrypted data, general response body
//                        objectMapper
//                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//
//                                // ignore unknown json properties to prevent HttpMessageNotReadableException
//                                // https://stackoverflow.com/a/5455563
////                objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
//                                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
////                                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
////                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
////                                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                ;
//                objectMapper.getDeserializationConfig().without(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

                mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
            }
        });
        log.debug("extendMessageConverters - end");
    }

    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("addInterceptors - start");
        //
        // The excluded path list should match to WebSecurityConfig.filterChain
        // "/auth/**" is the oauth2 login flow url
        List<String> excludedPathPatternList = List.of(
                "/"
                , "/login"
                , "/logout"
                , "/error"
//                , "/auth/**"
                , PublicControllerHelper.resolveExcludePath()
        );
        registry.addInterceptor(correlationIdInterceptor)
                .addPathPatterns("/**")
        ;
        //
        registry.addInterceptor(authorizationDataInterceptor)
//                .addPathPatterns("/**")
////                .excludePathPatterns("/auth/**", "/logout")
                .excludePathPatterns(excludedPathPatternList.toArray(String[]::new))
//                .excludePathPatterns(PublicControllerHelper.resolveExcludePath())
        ;
        registry.addInterceptor(permissionControlInterceptor)
                .excludePathPatterns(excludedPathPatternList.toArray(String[]::new))
        ;
        log.debug("addInterceptors - end");
    }

}
