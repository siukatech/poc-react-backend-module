package com.siukatech.poc.react.backend.module.core.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
//    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("doFilterInternal - start");
        log.debug("doFilterInternal - handlerExceptionResolver: [{}]", handlerExceptionResolver);
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("doFilterInternal - Spring Security filter chain exception: [{}]", ex.getMessage());
            this.handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
