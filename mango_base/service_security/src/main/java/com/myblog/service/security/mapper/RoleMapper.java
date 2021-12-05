package com.myblog.service.security.mapper;

import com.myblog.service.security.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
}
