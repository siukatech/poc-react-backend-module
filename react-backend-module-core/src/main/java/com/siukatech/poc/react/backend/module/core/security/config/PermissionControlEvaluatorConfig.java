package com.siukatech.poc.react.backend.module.core.security.config;

import com.siukatech.poc.react.backend.module.core.security.evaluator.DefaultRbacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.DefaultRlacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RbacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RlacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This config is created because Component + ConditionalOnMissingBean will cause the bean creation ordering issue
 * The components will be created later than user bean, then "No qualifying bean of type" will be thrown
 */
@Slf4j
@Configuration
public class PermissionControlEvaluatorConfig {

    @Bean
    @ConditionalOnMissingBean(value = {RbacPermissionControlEvaluator.class})
    public RbacPermissionControlEvaluator rbacEvaluator() {
        RbacPermissionControlEvaluator rbacPermissionControlEvaluator = new DefaultRbacPermissionControlEvaluator();
        return rbacPermissionControlEvaluator;
    }

    @Bean
    @ConditionalOnMissingBean(value = {RlacPermissionControlEvaluator.class})
    public RlacPermissionControlEvaluator rlacEvaluator(ResourceCheckManager resourceCheckManager) {
        RlacPermissionControlEvaluator rlacPermissionControlEvaluator = new DefaultRlacPermissionControlEvaluator(resourceCheckManager);
        return rlacPermissionControlEvaluator;
    }
}
