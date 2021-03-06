package com.kalix.framework.core.api.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KalixCascade {
    public static String alias = "KalixCascade";

    String beans() default "";

    boolean deletable() default false;

    String foreignKey() default "";
}