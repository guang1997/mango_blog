package com.myblog.service.security.service.impl;

import com.myblog.service.security.entity.Permission;
import com.myblog.service.security.mapper.PermissionMapper;
import com.myblog.service.security.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * API权限表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getApiUrlByUserName(String username) {
        return permissionMapper.getApiUrlByUserName(username);
    }
}
