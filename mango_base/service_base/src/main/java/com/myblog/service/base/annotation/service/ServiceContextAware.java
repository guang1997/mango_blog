package com.myblog.service.base.annotation.service;

import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.entity.dto.BaseDto;

import java.util.Objects;

public interface ServiceContextAware {

    default String getServiceName() {
        ServiceContext serviceContext = getClass().getAnnotation(ServiceContext.class);
        if (Objects.nonNull(serviceContext)) {
            return serviceContext.serviceName();
        }
        return "";
    }

    default Class<? extends BaseEntity> getDbClass() {
        ServiceContext serviceContext = getClass().getAnnotation(ServiceContext.class);
        if (Objects.nonNull(serviceContext)) {
            return serviceContext.dbClazz();
        }
        return BaseEntity.class;
    }

    default Class<? extends BaseDto> getDtoClass() {
        ServiceContext serviceContext = getClass().getAnnotation(ServiceContext.class);
        if (Objects.nonNull(serviceContext)) {
            return serviceContext.dtoClazz();
        }
        return BaseDto.class;
    }
}
