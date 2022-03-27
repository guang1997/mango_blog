package com.myblog.service.base.handler;

import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.base.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface ServiceConvertHandler<A extends BaseEntity, B extends BaseDto> {

    default List<B> toDtoList(List<A> dbs, Class<B> dtoClazz) throws IllegalAccessException, InstantiationException {
        List<B> dtoList = new ArrayList<>();
        for (A db : dbs) {
            B dto = dtoClazz.newInstance();
            BeanUtil.copyProperties(db, dto);
            setDtoExtraProperties(db, dto);
            dtoList.add(dto);
        }
        return dtoList;
    }

    default B toDto(A db, Class<B> dtoClazz) throws IllegalAccessException, InstantiationException {
        B dto = dtoClazz.newInstance();
        BeanUtil.copyProperties(db, dto);
        setDtoExtraProperties(db, dto);
        return dto;
    }

    default void setDtoExtraProperties(A db, B dto) {
        dto.setId(db.getId());
        dto.setCreateTime(db.getCreateTime());
        dto.setUpdateTime(db.getUpdateTime());
    }

    default A toDb(B dto, Class<A> dbClazz) throws IllegalAccessException, InstantiationException {
        A db = dbClazz.newInstance();
        BeanUtil.copyProperties(dto, db);
        setDbExtraProperties(db, dto);
        return db;
    }

    default void setDbExtraProperties(A db, B dto) {
        if (StringUtils.isNotBlank(dto.getId())) {
            db.setId(dto.getId());
        }
    }
}
