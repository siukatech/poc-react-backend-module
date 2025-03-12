package com.siukatech.poc.react.backend.module.core.web.interceptor;

import com.siukatech.poc.react.backend.module.core.web.micrometer.CorrelationIdHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class CorrelationIdInterceptor implements HandlerInterceptor {

    private final CorrelationIdHandler correlationIdHandler;

    public CorrelationIdInterceptor(CorrelationIdHandler correlationIdHandler) {
        this.correlationIdHandler = correlationIdHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler) throws Exception {
        log.debug("preHandle - start");
        log.debug("preHandle - request.getRequestURI: [{}]", request.getRequestURI());

        response.setHeader(CorrelationIdHandler.HEADER_X_CORRELATION_ID
                , this.correlationIdHandler.getCorrelationId());

        log.debug("preHandle - end");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.debug("postHandle - start");

        log.debug("preHandle - request.getRequestURI: [${}]", request.getRequestURI());
        log.debug("preHandle - request.HEADER_X_CORRELATION_ID: [${}], correlationIdHandler.getCorrelationId: [{}]"
                , response.getHeader(CorrelationIdHandler.HEADER_X_CORRELATION_ID)
                , this.correlationIdHandler.getCorrelationId()
        );

        log.debug("postHandle - end");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response
            , Object handler, @Nullable Exception ex) throws Exception {
        log.debug("postHandle - start");

        log.debug("postHandle - end");
    }

}
