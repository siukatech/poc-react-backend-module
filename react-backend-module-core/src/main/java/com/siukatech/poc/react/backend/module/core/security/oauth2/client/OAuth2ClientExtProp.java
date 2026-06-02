package com.siukatech.poc.react.backend.module.core.security.oauth2.client;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
@Component
@Primary // Tells Spring to use your bean instead of the default one
@ConfigurationProperties("spring.security.oauth2.client")
public class OAuth2ClientExtProp extends OAuth2ClientProperties {

    /**
     * OAuth provider details.
     */
    @Getter
    private final Map<String, OAuth2ClientExtProp.Provider> providerExt = new HashMap<>();

    // 2. Custom setter catches property binding under 'spring.security.oauth2.client.provider'
    public void setProvider(Map<String, Provider> provider) {
        log.debug("setProvider - provider: [{}]", provider);
        if (provider != null) {
            this.providerExt.putAll(provider);
            // Crucial: Push the data up into Spring's native Map so internal filters work
            super.getProvider().putAll(provider);
        }
    }

    public void afterPropertiesSet() {
        log.debug("afterPropertiesSet - getProvider: [{}]", this.getProvider());
        super.afterPropertiesSet();
    }

    @Getter
    @Setter
    public static class Provider extends OAuth2ClientProperties.Provider {
        private String issuerExt;
    }

}
