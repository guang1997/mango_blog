package com.myblog.service.base.annotation.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogByMethod {
    /**
     * 方法注释
     * @return
     */
    String value() default "";
}
