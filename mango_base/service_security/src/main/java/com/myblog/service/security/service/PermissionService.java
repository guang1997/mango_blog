package com.myblog.service.security.service;

import com.myblog.service.security.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * API权限表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> getApiUrlByUserName(String username);
}
