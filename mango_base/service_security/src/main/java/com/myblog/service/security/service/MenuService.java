package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.security.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.MenuDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-05
 */
public interface MenuService extends IService<Menu> {

    List<Menu> getMenuByRoles(List<Role> roles);

    MenuDto getMenuById(String id);

    List<MenuDto> getMenusByPid(String pid);

    List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menuDtos);

    Response addMenu(MenuDto menuDto);

    Response editMenu(MenuDto menuDto);

    Response delMenu(List<String> ids);

    Set<MenuDto> getChildren(List<MenuDto> childrenList, Set<MenuDto> menuDtos);
}
