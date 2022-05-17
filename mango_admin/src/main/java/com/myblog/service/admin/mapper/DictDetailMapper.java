package com.myblog.service.admin.mapper;

import com.myblog.service.admin.entity.DictDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 字典详细数据表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
public interface DictDetailMapper extends BaseMapper<DictDetail> {

    int updateByDictLabel(DictDetail dictDetail);

    List<DictDetail> getDetailsByDictName(String dictName);
}
