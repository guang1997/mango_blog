package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
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
public interface RoleService extends IService<Role>, ServiceConvertHandler<Role, RoleDto> {

    List<Role> getRolesByUserId(String userId) ;

    Response getRoleByPage(RoleDto roleDto) throws Exception;

    Response addRole(RoleDto roleDto) throws Exception;

    Response delRole(Set<String> ids) throws Exception;

    Response editRole(RoleDto roleDto) throws Exception;

    Response updateMenu(RoleDto roleDto) throws Exception;

    Response getRoleById(Role role) throws Exception;

    void validRoleLevelByUserId(String userId);

    void validRoleLevel(Integer level, String roleName);
}
