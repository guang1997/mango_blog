package com.myblog.service.base.util;

import com.myblog.service.base.entity.BaseEntity;
import com.myblog.service.base.entity.dto.BaseDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现Db和Dto的相互转换
 *
 * @author 李斯特
 * @date 2022/03/24
 */
public class DbConverter<A extends BaseEntity, B extends BaseDto> {

    public List<B> toDto(List<A> dbs, Class<B> bClass) throws IllegalAccessException, InstantiationException {
        List<B> dtoList = new ArrayList<>();
        for (A db : dbs) {
            B b = bClass.newInstance();
            BeanUtil.copyProperties(db, b);
            b.setId(db.getId());
            b.setCreateTime(db.getCreateTime());
            dtoList.add(b);
        }
        return dtoList;
    }
}
