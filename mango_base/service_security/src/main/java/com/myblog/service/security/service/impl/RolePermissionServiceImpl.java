package com.myblog.service.security.service.impl;

import com.myblog.service.security.entity.RolePermission;
import com.myblog.service.security.mapper.RolePermissionMapper;
import com.myblog.service.security.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * API权限表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
