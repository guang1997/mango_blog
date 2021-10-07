package com.myblog.service.security.service.impl;

import com.myblog.service.security.entity.Role;
import com.myblog.service.security.mapper.RoleMapper;
import com.myblog.service.security.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByUserName(String username) {
        return roleMapper.getRolesByUserName(username);
    }
}
