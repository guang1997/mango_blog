package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.DictDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.base.common.Response;

import java.util.Set;

/**
 * <p>
 * 字典详细数据表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
public interface DictDetailService extends IService<DictDetail> {

    Response getDictDetailByPage(DictDetailDto dictDetailDto);

    Response editDictDetail(DictDetailDto dictDetailDto);

    Response delDictDetails(Set<String> ids);

    Response addDictDetail(DictDetailDto dictDetailDto);

    Response getDetailsByDictName(String dictName);
}
