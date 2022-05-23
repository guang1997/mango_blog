package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.DictDetail;
import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Set;

/**
 * <p>
 * 字典详细数据表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
public interface DictDetailService extends IService<DictDetail>, ServiceConvertHandler<DictDetail, DictDetailDto> {

    Response getDictDetailByPage(DictDetailDto dictDetailDto) throws Exception;

    Response editDictDetail(DictDetailDto dictDetailDto) throws Exception;

    Response delDictDetails(Set<String> ids) throws Exception;

    Response addDictDetail(DictDetailDto dictDetailDto) throws Exception;

    Response getDetailsByDictName(String dictName);
}