package com.siukatech.poc.react.backend.module.core.security.aop;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RbacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlExceptionRec;
import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlNotFoundException;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.NoneResourceChecker;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceCheckResult;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceChecker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Aspect
//@Component
//@Lazy
public class PermissionControlAspect {

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final RbacPermissionControlEvaluator rbacPermissionControlEvaluator;
    private final ApplicationContext applicationContext;

    public PermissionControlAspect(
            RbacPermissionControlEvaluator rbacPermissionControlEvaluator
            , ApplicationContext applicationContext
    ) {
        this.rbacPermissionControlEvaluator = rbacPermissionControlEvaluator;
        this.applicationContext = applicationContext;
    }

    //    @Before("@annotation(com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl)")
//    public void evaluate(JoinPoint joinPoint) throws Throwable {
    @Before("@annotation(permissionControl)")
    public void evaluate(JoinPoint joinPoint, PermissionControl permissionControl) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication == null ? "NULL" : authentication.getName();
        log.debug("evaluate - userId: [{}], start", userId);

        String controllerName = joinPoint.getTarget().getClass().getName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
//        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        String methodName = method.getName();
//        PermissionControl permissionControl = AnnotationUtils.findAnnotation(
//                method, PermissionControl.class);

        // ==========================================
        // 2. RBAC Evaluation (API-Level Permission)
        // ==========================================
        if (StringUtils.hasText(permissionControl.accessRight())) {
            log.info("evaluate - [Security-RBAC] Checking API [{}] accessRight [{}] for method [{}]"
                    , permissionControl.appResourceId(), permissionControl.accessRight(), methodName);
            boolean rbacPassed = false;
            rbacPassed = rbacPermissionControlEvaluator.evaluate(permissionControl, method, authentication);
            if (!rbacPassed) {
                log.warn("evaluate - [Security-RBAC] Denied! Method [{}] AppResourceId [{}] requires permission [{}]"
                        , methodName, permissionControl.appResourceId(), permissionControl.accessRight());
                throw new PermissionControlNotFoundException("RBAC Permission Denied");
            }
        }

        // ==========================================
        // 3. RLAC Evaluation (Resource-Level Control)
        // ==========================================
        ResourceCheck resourceCheck = permissionControl.resourceCheck();
        if (Objects.nonNull(resourceCheck)) {
            Class<? extends ResourceChecker> resourceCheckerClazz = resourceCheck.resourceChecker();
            String checkMethod = resourceCheck.checkMethod();
            boolean skipChecker = resourceCheck.skipChecker();

            // Resolve method name: Use explicit name if provided, else default to controller method name
            String targetCheckMethodName = StringUtils.hasText(checkMethod)
                    ? resourceCheck.checkMethod()
                    : methodName;

            log.info("evaluate - [Security-RLAC] Checking appResourceId: [{}]"
                            + ", controllerName: [{}], methodName: [{}]"
                            + ", resourceCheckerClazz: [{}], checkMethod: [{}]"
                            + ", targetCheckMethodName: [{}]"
                            + ", skipChecker: [{}]"
                    , permissionControl.appResourceId()
                    , controllerName, methodName
                    , resourceCheckerClazz.getSimpleName(), checkMethod
                    , targetCheckMethodName
                    , skipChecker
            );

            // 1. Bypass immediately if skipChecker is true
            if (skipChecker) {
                log.warn("evaluate - [Security-RLAC] skipChecker - controllerName: [{}], methodName: [{}]"
                        + ", resourceCheckerClazz: [{}], checkMethod: [{}], targetCheckMethodName: [{}]"
                        , controllerName, methodName
                        , resourceCheckerClazz.getSimpleName(), checkMethod
                        , targetCheckMethodName
                );
                return;
            }

            // 2. Validate that a real checker class was specified when skipChecker is false
            if (resourceCheckerClazz == NoneResourceChecker.class) {
                throw new IllegalArgumentException(
                        "A valid ResourceChecker class must be specified on @ResourceCheck unless 'skipChecker = true'."
                );
            }

            Object[] args = joinPoint.getArgs();
            Object checkerBean = this.applicationContext.getBean(resourceCheckerClazz);

            // Find matching method on target bean using resolved method name
            Method targetCheckMethod = this.findMatchingMethod(resourceCheckerClazz, targetCheckMethodName, args);

            if (targetCheckMethod == null) {
                throw new IllegalStateException(
                        String.format("No matching method '%s' found in %s with %d arguments.",
                                methodName, resourceCheckerClazz.getName(), args.length)
                );
            }

            // 3. Invoke permission check
            ResourceCheckResult resourceCheckResult;
            try {
                resourceCheckResult = (ResourceCheckResult) targetCheckMethod.invoke(checkerBean, args);
            } catch (InvocationTargetException e) {
                // Rethrow target exception if checker method throws custom exception
                throw e.getCause();
            }
            if (!resourceCheckResult.isHasAccess()) {
                // Format using Stream
                String outputMapStr = resourceCheckResult.getOutputMap().entrySet()
                        .stream()
                        .map(entry -> String.format("key: [%s], value: [%s]", entry.getKey(), entry.getValue()))
                        .collect(Collectors.joining(", "));
//                throw new AccessDeniedException(
//                        String.format("Access denied by %s.%s, outputMapStr: [%s]", resourceCheckerClazz.getSimpleName(), methodName, outputMapStr)
//                );
                PermissionControlExceptionRec permissionControlExceptionRec = new PermissionControlExceptionRec(
                        authentication
                        , controllerName, methodName
                        , Objects.nonNull(permissionControl) ? permissionControl.toString() : "NULL"
                        , Objects.nonNull(permissionControl) ? permissionControl.appResourceId() : "NULL"
                        , Objects.nonNull(permissionControl) ? permissionControl.accessRight() : "NULL"
                        , resourceCheckerClazz.getName()
                        , checkMethod
                        , String.valueOf(skipChecker)
                        , (mga) -> {}
                );
                throw PermissionControlNotFoundException.toPermissionControlNotFoundException(permissionControlExceptionRec);
            }
        }
        else {
            PermissionControlExceptionRec permissionControlExceptionRec = new PermissionControlExceptionRec(
                    authentication
                    , controllerName, methodName
                    , "NULL"
                    , "NULL", "NULL"
                    , "NULL", "NULL"
                    , "NULL"
                    , (mga) -> {}
            );
            throw PermissionControlNotFoundException.toPermissionControlNotFoundException(permissionControlExceptionRec);
        }
    }

    private Method findMatchingMethod(Class<?> clazz, String methodName, Object[] args) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == args.length) {
                if (isAssignable(method.getParameterTypes(), args)) {
                    return method;
                }
            }
        }
        return null;
    }

    private boolean isAssignable(Class<?>[] paramTypes, Object[] args) {
        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] != null) {
                // Handle primitive wrappers (e.g. Integer.TYPE vs java.lang.Integer)
                Class<?> paramType = wrapPrimitive(paramTypes[i]);
                if (!paramType.isAssignableFrom(args[i].getClass())) {
                    return false;
                }
            }
        }
        return true;
    }

    private Class<?> wrapPrimitive(Class<?> clazz) {
        if (!clazz.isPrimitive()) return clazz;
        if (clazz == int.class) return Integer.class;
        if (clazz == long.class) return Long.class;
        if (clazz == boolean.class) return Boolean.class;
        if (clazz == double.class) return Double.class;
        if (clazz == float.class) return Float.class;
        if (clazz == byte.class) return Byte.class;
        if (clazz == char.class) return Character.class;
        if (clazz == short.class) return Short.class;
        return clazz;
    }

}