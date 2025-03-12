package com.siukatech.poc.react.backend.module.core.security.interceptor;

import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class AuthorizationDataInterceptor implements HandlerInterceptor {

    private final AuthorizationDataProvider authorizationDataProvider;

    public AuthorizationDataInterceptor(AuthorizationDataProvider authorizationDataProvider) {
        this.authorizationDataProvider = authorizationDataProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler) throws Exception {
        log.debug("preHandle - start");
        log.debug("preHandle - request.getRequestURI: [${}]", request.getRequestURI());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
//            myAuthenticationToken.getName();
        }
        log.debug("preHandle - authentication.name: [{}]"
                , authentication == null ? "NULL" : authentication.getName()
        );


        log.debug("preHandle - end");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.debug("postHandle - start");

        log.debug("postHandle - end");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response
            , Object handler, @Nullable Exception ex) throws Exception {
        log.debug("postHandle - start");

        log.debug("postHandle - end");
    }

}
