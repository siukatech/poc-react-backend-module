package com.siukatech.poc.react.backend.module.core.security.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Reference:
 * https://www.baeldung.com/spring-security-exceptionhandler#1configuring-authenticationentrypoint
 * <p>
 * Create the DelegatedAuthenticationEntryPoint to transfer exception from authentication filter to ExceptionHandler.
 */
@Slf4j
//@Component("delegatedAuthenticationEntryPoint")
@Component
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

//    private final HandlerExceptionResolver handlerExceptionResolver;
    @Autowired
//    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

//    public DelegatedAuthenticationEntryPoint(
//            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver
//    ) {
//        this.handlerExceptionResolver = handlerExceptionResolver;
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException authException) throws IOException, ServletException {
        log.debug("commence - start");
        this.handlerExceptionResolver.resolveException(request, response, null, authException);
        log.debug("commence - end");
    }
}
