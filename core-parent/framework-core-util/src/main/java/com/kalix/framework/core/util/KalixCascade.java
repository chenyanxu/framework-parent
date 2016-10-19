package com.kalix.framework.core.util;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface KalixCascade {
    public static String alias = "KalixCascade";

    String beans() default "";

    boolean deletable() default false;

    String foreignKey() default "";
}