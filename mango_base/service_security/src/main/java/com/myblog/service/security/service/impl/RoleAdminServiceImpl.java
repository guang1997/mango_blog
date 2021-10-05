package com.myblog.service.security.service.impl;

import com.myblog.service.security.entity.RoleAdmin;
import com.myblog.service.security.mapper.RoleAdminMapper;
import com.myblog.service.security.service.RoleAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色管理员中间表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Service
public class RoleAdminServiceImpl extends ServiceImpl<RoleAdminMapper, RoleAdmin> implements RoleAdminService {

}
