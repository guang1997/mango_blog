package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.base.common.Response;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-08
 */
public interface SysDictDataService extends IService<SysDictData> {

    Response getListByDictType(String dictType);
}
