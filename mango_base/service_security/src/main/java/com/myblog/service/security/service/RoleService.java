package com.myblog.service.security.service;

import com.myblog.service.security.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface RoleService extends IService<Role> {

    List<Role> getRolesByUserName(String username);
}
