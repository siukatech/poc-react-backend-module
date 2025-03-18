//package com.siukatech.poc.react.backend.module.core.security.config;
//
//import com.siukatech.poc.react.backend.module.core.web.security.DelegatedAuthenticationEntryPoint;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
//
//@Configuration
//public class AuthenticationEntryPointConfig {
//
//    @Bean
//    public HandlerExceptionResolver handlerExceptionResolver() {
//        return new DefaultHandlerExceptionResolver();
//    }
//
//    @Bean
//    public AuthenticationEntryPoint delegatedAuthenticationEntryPoint() {
//        return new DelegatedAuthenticationEntryPoint(handlerExceptionResolver());
//    }
//
//}
