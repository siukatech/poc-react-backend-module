package com.siukatech.poc.react.backend.module.core.security.aop;

import com.siukatech.poc.react.backend.module.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.module.core.security.annotation.ResourceCheck;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RbacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.evaluator.RlacPermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.exception.PermissionControlNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class PermissionControlAspect {

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    private final RbacPermissionControlEvaluator rbacPermissionControlEvaluator;
    private final RlacPermissionControlEvaluator rlacPermissionControlEvaluator;

    public PermissionControlAspect(RbacPermissionControlEvaluator rbacPermissionControlEvaluator, RlacPermissionControlEvaluator rlacPermissionControlEvaluator) {
        this.rbacPermissionControlEvaluator = rbacPermissionControlEvaluator;
        this.rlacPermissionControlEvaluator = rlacPermissionControlEvaluator;
    }

    @Before("@annotation(permissionControl)")
    public void authorize(JoinPoint joinPoint, PermissionControl permissionControl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication == null ? "NULL" : authentication.getName();

        log.debug("authorize - userId: [{}], start", userId);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        // ==========================================
        // 1. Core Architecture Constraint Validation
        // ==========================================
        long expectedCheckCount = Arrays.stream(method.getParameters())
                // Filter parameters that act as sensitive resource identifiers
                .filter(p -> p.isAnnotationPresent(PathVariable.class) || p.isAnnotationPresent(RequestParam.class))
                // Exclude boolean switches, only count standard ID types
                .filter(p -> p.getType() == String.class || p.getType() == Long.class)
                .count();

        int actualCheckCount = permissionControl.resources().length;
        log.debug("authorize - userId: [{}], expectedCheckCount: [{}], actualCheckCount: [{}]"
                , userId, expectedCheckCount, actualCheckCount);

        // Strict Whitelist Rule: The number of declared @ResourceCheck must match the number of resource arguments
        if (actualCheckCount != expectedCheckCount) {
            log.error("[Security-Constraint] Architecture Violation in method [{}]: "
                            + "Expected {} resource checks based on method parameters, but found {} registered in @PermissionControl"
                    , methodName, expectedCheckCount, actualCheckCount);
            throw new IllegalStateException("Security Configuration Error: Resource check count mismatch");
        }

        // ==========================================
        // 2. RBAC Evaluation (API-Level Permission)
        // ==========================================
        if (StringUtils.hasText(permissionControl.accessRight())) {
            log.info("[Security-RBAC] Checking API [{}] accessRight [{}] for method [{}]"
                    , permissionControl.appResourceId(), permissionControl.accessRight(), methodName);
            boolean rbacPassed = rbacPermissionControlEvaluator.evaluate(permissionControl, authentication);
            if (!rbacPassed) {
                log.warn("[Security-RBAC] Denied! Method [{}] AppResourceId [{}] requires permission [{}]"
                        , methodName, permissionControl.appResourceId(), permissionControl.accessRight());
                throw new PermissionControlNotFoundException("RBAC Permission Denied");
            }
        }

        // ==========================================
        // 3. RLAC Evaluation (Resource-Level Control)
        // ==========================================
        ResourceCheck[] resourceChecks = permissionControl.resources();
        if (resourceChecks.length > 0) {
            Object[] args = joinPoint.getArgs();
            EvaluationContext evaluationContext = new MethodBasedEvaluationContext(joinPoint.getTarget(), method, args, nameDiscoverer);

            // Context container to pass validated parent resources down the chain
            Map<String, String> validatedResources = new LinkedHashMap<>();

            for (ResourceCheck resourceCheck : resourceChecks) {
                String currentType = resourceCheck.resourceType().toUpperCase();

                // Evaluate dynamic condition SpEL expression
                Boolean shouldCheck = expressionParser.parseExpression(resourceCheck.condition()).getValue(evaluationContext, Boolean.class);

                // If condition evaluates to false, log it clearly and skip the runtime validation
                if (shouldCheck != null && !shouldCheck) {
                    log.info("[Security-RLAC] Skip resourceCheck for Resource [{}], condition [{}] evaluated to FALSE on method [{}]",
                            currentType, resourceCheck.condition(), methodName);
                    continue;
                }

                // Resolve the actual Resource ID value from SpEL context
                Object resourceIdObj = expressionParser.parseExpression(resourceCheck.idExpression()).getValue(evaluationContext);
                if (resourceIdObj == null) {
                    log.error("[Security-RLAC] Error! Resource ID expression [{}] returned null on method [{}]"
                            , resourceCheck.idExpression(), methodName);
                    throw new AccessDeniedException("Required resource ID is null");
                }
                String resourceId = resourceIdObj.toString();

                // Print the validation log before execution
                log.info("[Security-RLAC] Verifying Resource [{}] with ID [{}] (AccessRight: {}), Context: {}",
                        currentType, resourceId, resourceCheck.accessRight(), validatedResources);

                // Execute the specific ResourceChecker strategy
                boolean rlacPassed = rlacPermissionControlEvaluator.evaluate(resourceCheck, validatedResources);
                if (!rlacPassed) {
                    log.warn("[Security-RLAC] Denied! No access to Resource [{}] with ID [{}] on method [{}]", currentType, resourceId, methodName);
                    throw new AccessDeniedException(String.format("RLAC Permission Denied for [%s]", currentType));
                }

                // Push current verified resource into the context map for subsequent checks
                validatedResources.put(currentType, resourceId);
            }
        }
    }
}