package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.security.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.security.entity.dto.RoleDto;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

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

    Response getRoleByPage(RoleDto roleDto) throws Exception;

    Response addRole(Role role);

    Response delRole(Set<String> ids);

    Response editRole(Role role);

    Response updateMenu(Role role);

    Response getRoleById(Role role);
}
