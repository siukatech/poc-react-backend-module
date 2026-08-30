package com.siukatech.poc.react.backend.module.core.security.annotation;

import com.siukatech.poc.react.backend.module.core.security.resourcechecker.NoneResourceChecker;
import com.siukatech.poc.react.backend.module.core.security.resourcechecker.ResourceChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceCheck {
//    String resourceType(); // Resource type, e.g., "SHOP", "CATEGORY"
//    String accessRight(); // Resource CRUD access right
//    //    String idExpression(); // Resource Id, SpEL expression, e.g., "#shopId"
//    String condition() default "true"; // SpEL conditional expression, defaults to "true"

    // Target checker class managed as a Spring Bean.
    // Optional if skipChecker is set to true.
    Class<? extends ResourceChecker> resourceChecker() default NoneResourceChecker.class;
    // Name of the permission check method to invoke.
    // If omitted or empty, defaults to the controller method name.
    String checkMethod() default "";
    // Set to true to bypass/skip executing the resource check method
    boolean skipChecker() default false;
}
