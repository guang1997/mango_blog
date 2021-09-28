package com.myblog.service.admin.service.impl;

import com.myblog.service.admin.entity.Role;
import com.myblog.service.admin.mapper.RoleMapper;
import com.myblog.service.admin.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
