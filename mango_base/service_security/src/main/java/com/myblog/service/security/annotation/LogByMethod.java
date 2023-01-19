package com.myblog.service.security.annotation;

import com.myblog.service.base.common.BehaviorEnum;

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

    /**
     * 是否检查入参
     * @return
     */
    boolean validate() default false;

    /**
     * 门户网站行为
     * @return
     */
    BehaviorEnum behavior() default BehaviorEnum.DEFAULT;

    /**
     * 是否打印出参
     * @return
     */
    boolean printResponse() default true;
}
