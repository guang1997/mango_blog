package com.myblog.service.admin.mapper;

import com.myblog.service.admin.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
public interface DictMapper extends BaseMapper<Dict> {

    int updateByDictName(Dict dict);
}
