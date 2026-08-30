package com.siukatech.poc.react.backend.module.core.security.config;

import com.siukatech.poc.react.backend.module.core.security.aop.PermissionControlAspect;
import com.siukatech.poc.react.backend.module.core.security.evaluator.DefaultRbacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RbacPermissionControlEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

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

//    @Lazy
    @Bean
    public PermissionControlAspect permissionControlAspect(
            RbacPermissionControlEvaluator rbacPermissionControlEvaluator
            , ApplicationContext applicationContext
    ) {
        return new PermissionControlAspect(rbacPermissionControlEvaluator, applicationContext);
//        return new PermissionControlAspect();
    }

}
