package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.Dict;
import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Set;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
public interface DictService extends IService<Dict>, ServiceConvertHandler<Dict, DictDto> {

    Response getDictByPage(DictDto dictDto) throws Exception;

    Response addDict(DictDto dictDto) throws Exception;

    Response editDict(DictDto dictDto) throws Exception;

    Response delDict(Set<String> ids) throws Exception;
}
