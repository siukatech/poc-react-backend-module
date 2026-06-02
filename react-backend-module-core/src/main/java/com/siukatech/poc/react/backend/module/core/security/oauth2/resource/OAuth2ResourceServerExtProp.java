package com.siukatech.poc.react.backend.module.core.security.oauth2.resource;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
//@Configuration
@Component
@Primary // Tells Spring to use your bean instead of the default one
@ConfigurationProperties("spring.security.oauth2.resource-server")
public class OAuth2ResourceServerExtProp implements InitializingBean {
    private final Map<String, OAuth2ResourceServerExtProp.Jwt> jwt = new HashMap<>();
    private final Map<String, OAuth2ResourceServerExtProp.Opaquetoken> opaquetoken = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("afterPropertiesSet - jwt: [{}], opaquetoken: [{}]", jwt, opaquetoken);
    }

    @Getter
    @Setter
    public static class Jwt extends OAuth2ResourceServerProperties.Jwt {
        private String issuerExt;
    }

    @Getter
    @Setter
    public static class Opaquetoken extends OAuth2ResourceServerProperties.Opaquetoken {
        private String issuerExt;
    }

}
