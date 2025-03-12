package com.siukatech.poc.react.backend.module.core.web.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reference:
 * https://stackoverflow.com/a/76432289
 *
 * Create a NOOP Tracer when there is no Tracer.
 *
 */
@ConditionalOnProperty(prefix = "management.tracing", name = "enabled", havingValue = "false")
@Configuration
public class NoopTracingConfig {

    @Bean
    Tracer tracer() {
        return Tracer.NOOP;
    }

    @Bean
    ObservationRegistry observationRegistry() {
        return ObservationRegistry.NOOP;
    }

}
