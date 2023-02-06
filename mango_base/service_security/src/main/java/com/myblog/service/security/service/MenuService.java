package com.myblog.service.security.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
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
public interface MenuService extends IService<Menu>, ServiceConvertHandler<Menu, MenuDto> {

    List<Menu> getMenuByRoles(List<Role> roles) throws Exception;

    MenuDto getMenuById(String id) throws Exception;

    List<MenuDto> getMenusByPid(String pid) throws Exception;

    List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menuDtos) throws Exception;

    Boolean addMenu(MenuDto menuDto) throws Exception;

    Boolean editMenu(MenuDto menuDto) throws Exception;

    Boolean delMenu(List<String> ids) throws Exception;

    Set<MenuDto> getChildren(List<MenuDto> childrenList, Set<MenuDto> menuDtos) throws Exception;
}
