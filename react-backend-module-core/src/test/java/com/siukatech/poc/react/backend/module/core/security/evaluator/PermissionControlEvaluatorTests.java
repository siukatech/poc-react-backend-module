package com.siukatech.poc.react.backend.module.core.security.evaluator;

import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.security.model.MyGrantedAuthority;
import com.siukatech.poc.react.backend.module.core.security.controller.ProtectedUrlController;
import com.siukatech.poc.react.backend.module.core.security.controller.RestUrlController;
import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlNotFoundException;
import com.siukatech.poc.react.backend.module.core.web.controller.WebController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {PermissionControlEvaluator.class})
public class PermissionControlEvaluatorTests extends AbstractUnitTests {
    @Autowired
    private PermissionControlEvaluator permissionControlEvaluator;

    private List<MyGrantedAuthority> getGrantAuthorityList() {
        String applicationId = "backend-app";
        String userRoleIdUser = "role-user-01";
        String userRoleIdAdmin = "role-admin-01";
        List<MyGrantedAuthority> authorities = List.of(
                MyGrantedAuthority.builder()
                        .applicationId(applicationId)
                        .userRoleId(userRoleIdUser)
//                        .appResourceId("core.web.authorized")
                        .appResourceId("core.protectedUrl.authorized")
                        .accessRight("view")
                        .build()
                , MyGrantedAuthority.builder()
                        .applicationId(applicationId)
                        .userRoleId(userRoleIdAdmin)
                        .appResourceId("core.restUrl.authorized")
                        .accessRight("view")
                        .build()
        );
        return authorities;
    }

    private Map<String, Object> getAttributeMap() {
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put(StandardClaimNames.PREFERRED_USERNAME, "app-user-01");
        attributeMap.put(MyAuthenticationToken.ATTR_TOKEN_VALUE, "tokenValue");
        attributeMap.put(MyAuthenticationToken.ATTR_USER_ID, 1L);
        attributeMap.put(MyAuthenticationToken.ATTR_PUBLIC_KEY, "public-key");
        return attributeMap;
    }

    @Test
    public void test_evaluate_basic_protectedUrlController() throws PermissionControlNotFoundException {
        // given
        Method method = ClassUtils.getMethod(ProtectedUrlController.class, "authorized");
        HandlerMethod handlerMethod = new HandlerMethod(new ProtectedUrlController(), method);
        List<MyGrantedAuthority> authorities = getGrantAuthorityList();
        Map<String, Object> attributeMap = getAttributeMap();
        OAuth2User principal = new DefaultOAuth2User(authorities, attributeMap, StandardClaimNames.PREFERRED_USERNAME);
        MyAuthenticationToken authentication = new MyAuthenticationToken(principal, principal.getAuthorities(), "keycloak");

        // when
        boolean result = permissionControlEvaluator.evaluate(handlerMethod, authentication);

        // then
        assertTrue(result);
    }

    @Test
    public void test_evaluate_basic_restUrlController() throws PermissionControlNotFoundException {
        // given
        Method method = ClassUtils.getMethod(RestUrlController.class, "authorized");
        HandlerMethod handlerMethod = new HandlerMethod(new RestUrlController(), method);
        List<MyGrantedAuthority> authorities = getGrantAuthorityList();
        Map<String, Object> attributeMap = getAttributeMap();
        OAuth2User principal = new DefaultOAuth2User(authorities, attributeMap, StandardClaimNames.PREFERRED_USERNAME);
        MyAuthenticationToken authentication = new MyAuthenticationToken(principal, principal.getAuthorities(), "keycloak");

        // when
        boolean result = permissionControlEvaluator.evaluate(handlerMethod, authentication);

        // then
        assertTrue(result);
    }

    @Test
    public void test_evaluate_basic_webController() throws PermissionControlNotFoundException {
        // given
        Method method = ClassUtils.getMethod(WebController.class, "authorized", Principal.class, Model.class);
        HandlerMethod handlerMethod = new HandlerMethod(new WebController(), method);
        List<MyGrantedAuthority> authorities = getGrantAuthorityList();
        Map<String, Object> attributeMap = getAttributeMap();
        OAuth2User principal = new DefaultOAuth2User(authorities, attributeMap, StandardClaimNames.PREFERRED_USERNAME);
        MyAuthenticationToken authentication = new MyAuthenticationToken(principal, principal.getAuthorities(), "keycloak");
//
//        // when
//        boolean result = permissionControlEvaluator.evaluate(handlerMethod, authentication);
//
//        // then
//        assertTrue(result);

        // when
        Exception exception = assertThrows(PermissionControlNotFoundException.class, () -> {
            boolean result = permissionControlEvaluator.evaluate(handlerMethod, authentication);
        });

        // then
        assertEquals(exception.getClass(), PermissionControlNotFoundException.class);
    }

    @Test
    public void test_evaluate_access_denied_protectedUrlController() throws PermissionControlNotFoundException {
        // given
        Method method = ClassUtils.getMethod(ProtectedUrlController.class, "accessDenied");
        HandlerMethod handlerMethod = new HandlerMethod(new ProtectedUrlController(), method);
        OAuth2User principal = new DefaultOAuth2User(getGrantAuthorityList(), getAttributeMap(), StandardClaimNames.PREFERRED_USERNAME);
        MyAuthenticationToken authentication = new MyAuthenticationToken(principal, principal.getAuthorities(), "keycloak");

        // when
        Exception exception = assertThrows(PermissionControlNotFoundException.class, () -> {
            boolean result = permissionControlEvaluator.evaluate(handlerMethod, authentication);
        });

        // then
        assertEquals(exception.getClass(), PermissionControlNotFoundException.class);
    }

    @Test
    public void test_evaluate_access_denied_restUrlController() throws PermissionControlNotFoundException {
        // given
        Method method = ClassUtils.getMethod(RestUrlController.class, "accessDenied");
        HandlerMethod handlerMethod = new HandlerMethod(new RestUrlController(), method);
        OAuth2User principal = new DefaultOAuth2User(getGrantAuthorityList(), getAttributeMap(), StandardClaimNames.PREFERRED_USERNAME);
        MyAuthenticationToken authentication = new MyAuthenticationToken(principal, principal.getAuthorities(), "keycloak");

        // when
        Exception exception = assertThrows(PermissionControlNotFoundException.class, () -> {
            boolean result = permissionControlEvaluator.evaluate(handlerMethod, authentication);
        });

        // then
        assertEquals(exception.getClass(), PermissionControlNotFoundException.class);
    }

}
