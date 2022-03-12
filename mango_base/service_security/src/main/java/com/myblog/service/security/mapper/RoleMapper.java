package com.myblog.service.security.mapper;

import com.myblog.service.security.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.service.security.entity.dto.MenuDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRolesByUserName(@Param("username") String username);

    List<String> getAdminIdsByRoleId(@Param("roleId") String roleId);

    int batchDeleteRoleAdminByRoleId(@Param("roleIds") List<String> roleIds);

    List<MenuDto> selectRoleMenu(String roleId);
}
