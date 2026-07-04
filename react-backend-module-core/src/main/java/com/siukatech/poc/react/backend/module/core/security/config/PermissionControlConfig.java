package com.siukatech.poc.react.backend.module.core.security.config;

import com.siukatech.poc.react.backend.module.core.security.aop.PermissionControlAspect;
import com.siukatech.poc.react.backend.module.core.security.evaluator.DefaultRbacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.DefaultRlacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RbacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RlacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;

/**
 * This config is created because Component + ConditionalOnMissingBean will cause the bean creation ordering issue
 * The components will be created later than user bean, then "No qualifying bean of type" will be thrown
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class PermissionControlConfig {

    @Bean
    @ConditionalOnMissingBean(value = {RbacPermissionControlEvaluator.class})
    public RbacPermissionControlEvaluator rbacPermissionControlEvaluator() {
        RbacPermissionControlEvaluator rbacPermissionControlEvaluator = new DefaultRbacPermissionControlEvaluator();
        return rbacPermissionControlEvaluator;
    }

    @Bean
    @ConditionalOnMissingBean(value = {RlacPermissionControlEvaluator.class})
    public RlacPermissionControlEvaluator rlacPermissionControlEvaluator(ResourceCheckManager resourceCheckManager) {
        RlacPermissionControlEvaluator rlacPermissionControlEvaluator = new DefaultRlacPermissionControlEvaluator(resourceCheckManager);
        return rlacPermissionControlEvaluator;
    }

//    @Lazy
    @Bean
    public PermissionControlAspect permissionControlAspect(
            RbacPermissionControlEvaluator rbacPermissionControlEvaluator
            , RlacPermissionControlEvaluator rlacPermissionControlEvaluator
    ) {
        return new PermissionControlAspect(rbacPermissionControlEvaluator, rlacPermissionControlEvaluator);
//        return new PermissionControlAspect();
    }

}
