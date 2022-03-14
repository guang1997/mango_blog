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

    List<Role> getRolesByUserId(String userId);

    Response getRoleByPage(RoleDto roleDto) throws Exception;

    Response addRole(RoleDto roleDto);

    Response delRole(Set<String> ids);

    Response editRole(RoleDto roleDto);

    Response updateMenu(RoleDto roleDto);

    Response getRoleById(Role role);
}
