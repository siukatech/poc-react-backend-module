package com.siukatech.poc.react.backend.module.core.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.security.interceptor.OAuth2ClientHttpRequestInterceptor;
import com.siukatech.poc.react.backend.module.core.web.micrometer.CorrelationIdHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
public class Oauth2ClientRestTemplateConfig {

//    private final ObjectMapper objectMapper;
//    private final CorrelationIdHandler correlationIdHandler;


    public Oauth2ClientRestTemplateConfig(
//            ObjectMapper objectMapper
//            , CorrelationIdHandler correlationIdHandler
    ) {
//        this.objectMapper = objectMapper;
//        this.correlationIdHandler = correlationIdHandler;
    }

    @Bean
    @DependsOn(value = {"restTemplateBuilder"})
    public RestTemplate oauth2ClientRestTemplate(
            ObjectMapper objectMapper
            , RestTemplateBuilder restTemplateBuilder
            , CorrelationIdHandler correlationIdHandler
    ) {
        // This is not working - start
//        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
//        restTemplateBuilder.additionalInterceptors(new ClientHttpRequestInterceptor() {
////            private final Logger log = LoggerFactory.getLogger(this.getClass());
//            private final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
//
//            @Override
//            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                log.debug("oauth2ClientRestTemplate - RestTemplateBuilder - ClientHttpRequestInterceptor - intercept - "
//                                + "authentication.getName: [{}], authentication.getCredentials: [{}]"
//                        , authentication.getName(), authentication.getCredentials());
////                String jwtToken = authentication.getCredentials().toString();
////                request.getHeaders().set(HttpHeaders.AUTHORIZATION, jwtToken);
////                return null;
//                return execution.execute(request, body);
//            }
//        });
        // This is not working - end

        RestTemplate restTemplate = restTemplateBuilder.build();
//        RestTemplate restTemplate = new RestTemplate();
        AtomicInteger formHttpMessageConverterCount = new AtomicInteger();

//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(this.objectMapper));
        restTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
////                ((MappingJackson2HttpMessageConverter) httpMessageConverter)
//                ObjectMapper objectMapper =
//                        mappingJackson2HttpMessageConverter
//                                .getObjectMapper();
//
//                objectMapper = objectMapper
//                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//
//                        // ignore unknown json properties to prevent HttpMessageNotReadableException
//                        // https://stackoverflow.com/a/5455563
//                        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
////                        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
//                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
////                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                ;
                mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
            }
            if (httpMessageConverter instanceof FormHttpMessageConverter) {
                formHttpMessageConverterCount.getAndIncrement();
            }
        });
        if (formHttpMessageConverterCount.get() == 0) {
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        }
        restTemplate.getInterceptors().add(new OAuth2ClientHttpRequestInterceptor(correlationIdHandler));
        log.debug("oauth2ClientRestTemplate - formHttpMessageConverterCount.get: [{}]"
                        + ", restTemplate.toString: [{}]"
                , formHttpMessageConverterCount.get(), restTemplate.toString()
        );
        return restTemplate;
    }

    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
        return new JwtGrantedAuthoritiesConverter();
    }

}
