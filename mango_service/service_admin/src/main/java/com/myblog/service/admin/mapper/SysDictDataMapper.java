package com.myblog.service.admin.mapper;

import com.myblog.service.admin.entity.SysDictData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典数据表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-08
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    List<SysDictData> getListByDictType(@Param("dictType") String dictType);
}
