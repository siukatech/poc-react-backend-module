package com.siukatech.poc.react.backend.module.core.security.interceptor;

import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import com.siukatech.poc.react.backend.module.core.security.evaluator.PermissionControlEvaluator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Reference:
 * https://medium.com/@aedemirsen/what-is-spring-boot-request-interceptor-and-how-to-use-it-7fd85f3df7f7
 */
@Slf4j
@Component
public class PermissionControlInterceptor implements HandlerInterceptor {

    private final PermissionControlEvaluator permissionControlEvaluator;

    public PermissionControlInterceptor(PermissionControlEvaluator permissionControlEvaluator) {
        this.permissionControlEvaluator = permissionControlEvaluator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
            , Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authName = authentication == null ? "NULL" : authentication.getName();

        log.debug("preHandle - authName: [{}], start", authName);
        log.debug("preHandle - authName: [{}], request.getRequestURI: [${}], handler: [{}]"
                , authName, request.getRequestURI(), handler);

        if (authentication instanceof MyAuthenticationToken myAuthenticationToken
                && myAuthenticationToken.getAuthorities() instanceof MyGrantedAuthority myGrantedAuthority) {
////            myAuthenticationToken.getName();
//            myGrantedAuthority.getAuthority();
            log.debug("preHandle - myAuthenticationToken - authName: [{}]"
                            + ", myGrantedAuthority.getAuthority: [{}]"
                    , authName
                    , myGrantedAuthority.getAuthority()
            );
        }
        log.debug("preHandle - authName: [{}]", authName);

        boolean result = false;
        if (handler instanceof HandlerMethod handlerMethod) {
            result = this.permissionControlEvaluator.evaluate(handlerMethod, authentication);
        }

        log.debug("preHandle - authName: [{}], result: [{}], end", authName, result);
        return result;
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
