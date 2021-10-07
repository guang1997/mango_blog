package com.myblog.service.security.mapper;

import com.myblog.service.security.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.service.security.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-05
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuByRoles(@Param("roles") List<Role> roles);
}
