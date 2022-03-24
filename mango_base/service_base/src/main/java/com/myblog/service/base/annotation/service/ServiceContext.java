package com.myblog.service.base.annotation.service;

import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.entity.dto.BaseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceContext {

    String serviceName() default "";

    Class<? extends BaseEntity> dbClazz() default BaseEntity.class;

    Class<? extends BaseDto> dtoClazz() default BaseDto.class;
}
