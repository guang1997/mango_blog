package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.security.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.entity.dto.RoleDto;

import java.util.List;
import java.util.Map;
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

    Map<String, Object> getRoleByPage(RoleDto roleDto) throws Exception;

    Boolean addRole(RoleDto roleDto) throws Exception;

    Boolean delRole(Set<String> ids) throws Exception;

    Boolean editRole(RoleDto roleDto) throws Exception;

    Boolean updateMenu(RoleDto roleDto) throws Exception;

    RoleDto getRoleById(Role role) throws Exception;

    Boolean validRoleLevelByUserId(String userId);

    Boolean validRoleLevel(Integer level, String roleName);

    List<String> getMenusByRoleId(List<String> roleIds);
}
