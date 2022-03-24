package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.base.annotation.service.ServiceContext;
import com.myblog.service.base.annotation.service.ServiceContextAware;
import com.myblog.service.base.common.Response;

import java.util.Set;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
public interface DictService extends IService<Dict>, ServiceContextAware {

    Response getDictByPage(DictDto dictDto) throws Exception;

    Response addDict(DictDto dictDto);

    Response editDict(DictDto dictDto);

    Response delDict(Set<String> ids);
}
