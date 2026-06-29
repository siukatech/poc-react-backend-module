package com.siukatech.poc.react.backend.module.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceCheck {
    String resourceType(); // Resource type, e.g., "SHOP", "CATEGORY"
    String accessRight(); // Resource CRUD access right
    String idExpression(); // Resource Id, SpEL expression, e.g., "#shopId"
    String condition() default "true"; // SpEL conditional expression, defaults to "true"
}