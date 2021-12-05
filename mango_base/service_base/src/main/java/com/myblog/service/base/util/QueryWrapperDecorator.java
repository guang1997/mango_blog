package com.myblog.service.base.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.DbConstants;

public class QueryWrapperDecorator<T> {

    public QueryWrapper<T> createBaseQueryWrapper() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.eq(DbConstants.Base.isDeleted, 0);
        wrapper.orderByAsc(DbConstants.Base.sort);
        return wrapper;
    }
}
