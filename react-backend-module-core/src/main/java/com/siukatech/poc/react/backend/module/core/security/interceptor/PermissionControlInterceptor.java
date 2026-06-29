package com.siukatech.poc.react.backend.module.core.security.interceptor;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlNotFoundException;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RbacPermissionControlEvaluator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Reference:
 * https://medium.com/@aedemirsen/what-is-spring-boot-request-interceptor-and-how-to-use-it-7fd85f3df7f7
 */
@Slf4j
@Component
public class PermissionControlInterceptor implements HandlerInterceptor {

    private final RbacPermissionControlEvaluator rbacPermissionControlEvaluator;

    public PermissionControlInterceptor(RbacPermissionControlEvaluator rbacPermissionControlEvaluator) {
        this.rbacPermissionControlEvaluator = rbacPermissionControlEvaluator;
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
        if (handler instanceof HandlerMethod handlerMethod && authentication != null) {
            result = this.evaluate(handlerMethod, authentication);
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


    protected boolean evaluate(HandlerMethod handlerMethod, Authentication authentication) throws PermissionControlNotFoundException {
        String userId = authentication.getName();
        Class<?> beanType = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        String beanName = beanType.getName();
        String methodName = method.getName();
        List<Annotation> methodAnnotationList = List.of(method.getAnnotations());
//        List<Annotation> beanTypeAnnotationLv1List = List.of(beanType.getAnnotations());
////        beanTypeAnnotationLv1List.stream().forEach(bta -> {
////            Annotation[] btaAnnotationList = bta.annotationType().getAnnotations();
////            log.debug("evaluate - bta.annotationType().getName: [{}], btaAnnotationList: [{}]"
////                    , bta.annotationType().getName(), btaAnnotationList);
////        });
//        List<Annotation> beanTypeAnnotationLv2List = List.of(beanType.getAnnotations()).stream()
//                .map(bta -> List.of(bta.annotationType().getAnnotations()))
//                .flatMap(list -> list.stream())
//                .collect(Collectors.toList())
//                ;
//        List<Annotation> beanTypeAnnotationAllList = new ArrayList<>();
//        beanTypeAnnotationAllList.addAll(beanTypeAnnotationLv1List);
//        beanTypeAnnotationAllList.addAll(beanTypeAnnotationLv2List);
//        log.debug("evaluate - beanTypeAnnotationLv1List: [{}], beanTypeAnnotationLv2List: [{}]"
//                , beanTypeAnnotationLv1List, beanTypeAnnotationLv2List);
////        boolean hasPublicController = beanTypeAnnotationAllList.stream().filter(a -> a.getClass().equals(PublicController.class)).count() > 0;
//        PublicController publicController = AnnotationUtils.findAnnotation(beanType, PublicController.class);
//        log.debug("evaluate - methodAnnotationList: [{}], publicController: [{}]"
//                , methodAnnotationList, publicController);

        PermissionControl permissionControlAnnotationByUtil = AnnotationUtils.findAnnotation(
                method, PermissionControl.class);
        PermissionControl permissionControlAnnotationByMethod = handlerMethod.getMethodAnnotation(PermissionControl.class);
//        boolean isPublic = publicController != null;
        boolean hasPrivilege = false;

        log.debug("evaluate - userId: [{}], beanName: [{}], methodName: [{}]"
//                        + ", hasPublicController: [{}]"
                , userId, beanName, methodName
//                , isPublic
        );
        log.debug("evaluate - userId: [{}], authentication.getClass.getName: [{}], permissionControlAnnotationByUtil: [{}], permissionControlAnnotationByMethod: [{}]"
                , userId, authentication.getClass().getName(), permissionControlAnnotationByUtil, permissionControlAnnotationByMethod);

        final PermissionControl permissionControl;
        permissionControl = permissionControlAnnotationByUtil;
//        permissionControl = permissionControlAnnotationByMethod;

//        if (isPublic) {
//            // nothing to do with PublicController
//        }
//        else {
        hasPrivilege = this.rbacPermissionControlEvaluator.evaluate(permissionControl, authentication);
//        }
        return hasPrivilege;
    }

}
