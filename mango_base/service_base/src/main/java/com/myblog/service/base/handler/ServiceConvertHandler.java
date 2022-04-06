package com.myblog.service.base.handler;

import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.base.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用于db和dto类的转换
 * 使用方法：service继承该类，同时A和B设置成对应的db类以及dto类
 *
 * @author 李斯特
 * 2022年4月4日
 */
public interface ServiceConvertHandler<A extends BaseEntity, B extends BaseDto> {

    /**
     * db集合转换为dto集合
     * @param dbs
     * @param dtoClazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
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

    /**
     * db转换为dto
     * @param db
     * @param dtoClazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    default B toDto(A db, Class<B> dtoClazz) throws IllegalAccessException, InstantiationException {
        B dto = dtoClazz.newInstance();
        BeanUtil.copyProperties(db, dto);
        setDtoExtraProperties(db, dto);
        return dto;
    }

    /**
     * 设置特殊字段，即BeanUtil不能复制的属性
     * @param db
     * @param dto
     */
    default void setDtoExtraProperties(A db, B dto) {
        dto.setId(db.getId());
        dto.setCreateTime(db.getCreateTime());
        dto.setUpdateTime(db.getUpdateTime());
    }

    /**
     * dto转换为db
     * @param dto
     * @param dbClazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    default A toDb(B dto, Class<A> dbClazz) throws IllegalAccessException, InstantiationException {
        A db = dbClazz.newInstance();
        BeanUtil.copyProperties(dto, db);
        setDbExtraProperties(db, dto);
        return db;
    }

    /**
     * 设置特殊字段，即BeanUtil不能复制的属性
     * @param db
     * @param dto
     */
    default void setDbExtraProperties(A db, B dto) {
        if (StringUtils.isNotBlank(dto.getId())) {
            db.setId(dto.getId());
        }
    }
}
