package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PermissionControlEvaluator {
    public boolean evaluate(HandlerMethod handlerMethod, Authentication authentication) throws PermissionControlNotFoundException {
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
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String appResourceId = (permissionControlAnnotationByUtil == null ? null : permissionControlAnnotationByUtil.appResourceId());
        String accessRight = (permissionControlAnnotationByUtil == null ? null : permissionControlAnnotationByUtil.accessRight());
        long authorityCount = -1;
//        boolean isPublic = publicController != null;

        log.debug("evaluate - userId: [{}], beanName: [{}], methodName: [{}]"
//                        + ", hasPublicController: [{}]"
                , userId, beanName, methodName
//                , isPublic
        );
        log.debug("evaluate - userId: [{}], authentication.getClass.getName: [{}], permissionControlAnnotationByUtil: [{}], permissionControlAnnotationByMethod: [{}]"
                , userId, authentication.getClass().getName(), permissionControlAnnotationByUtil, permissionControlAnnotationByMethod);
//        if (isPublic) {
//            // nothing to do with PublicController
//        }
//        else {
            if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
                grantedAuthorityList.addAll(myAuthenticationToken.getAuthorities());
                log.debug("evaluate - userId: [{}], grantedAuthorityList.size: [{}]"
                        , userId, grantedAuthorityList.size());
                log.trace("evaluate - userId: [{}], grantedAuthorityList: [{}]"
                        , userId, grantedAuthorityList);
                authorityCount = grantedAuthorityList.stream()
                        .filter(grantedAuthority -> grantedAuthority instanceof MyGrantedAuthority)
                        .map(MyGrantedAuthority.class::cast)
                        .peek(mga -> {
                            log.trace("evaluate - userId: [{}], permissionControlAnnotationByUtil: [{}]"
                                            + ", appResourceId: [{}], accessRight: [{}]"
                                            + ", mga.getApplicationId: [{}], mga.getUserRoleId: [{}]"
                                            + ", mga.getAppResourceId: [{}], mga.getAccessRight: [{}]"
                                            + ", mga.getAuthority: [{}]"
                                    , userId, permissionControlAnnotationByUtil
                                    , appResourceId, accessRight
                                    , mga.getApplicationId(), mga.getUserRoleId()
                                    , mga.getAppResourceId(), mga.getAccessRight()
                                    , mga.getAuthority()
                            );
                        })
                        .filter(mga -> mga.getAppResourceId() != null
                                && mga.getAppResourceId().equals(appResourceId)
                                && mga.getAccessRight() != null
                                && mga.getAccessRight().equals(accessRight)
                        )
                        .count();
                log.debug("evaluate - userId: [{}], permissionControlAnnotationByUtil: [{}], appResourceId: [{}], accessRight: [{}], authorityCount: [{}]"
                        , userId, permissionControlAnnotationByUtil, appResourceId, accessRight, authorityCount
                );
            }
            if (authorityCount <= 0) {
                String accessDeniedTmpl = "Access denied"
                        + ", myAuthenticationToken.getAuthorities.size: [%s]"
                        + ", myAuthenticationToken.getAuthorities.MyGrantedAuthority.count: [%s]"
                        + ", userId: [%s], beanName: [%s], methodName: [%s]"
                        + ", permissionControlAnnotationByUtil: [%s]"
                        + ", appResourceId: [%s], accessRight: [%s]"
                        + ", authorityCount: [%d]";
                String accessDeniedMsg = String.format(accessDeniedTmpl
                        , grantedAuthorityList.size()
                        , grantedAuthorityList.stream().filter(ga -> ga instanceof MyGrantedAuthority).count()
                        , userId, beanName, methodName
                        , permissionControlAnnotationByUtil == null ? "NULL" : permissionControlAnnotationByUtil.toString()
                        , appResourceId, accessRight
                        , authorityCount);
                throw new PermissionControlNotFoundException(accessDeniedMsg);
            }
//        }
        return true;
    }
}
