package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.DictDetail;
import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Map;
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

    Map<String, Object> getDictDetailByPage(DictDetailDto dictDetailDto) throws Exception;

    Boolean editDictDetail(DictDetailDto dictDetailDto) throws Exception;

    Boolean delDictDetails(Set<String> ids) throws Exception;

    Boolean addDictDetail(DictDetailDto dictDetailDto) throws Exception;

    Map<String, Object> getDetailsByDictName(String dictName);
}
