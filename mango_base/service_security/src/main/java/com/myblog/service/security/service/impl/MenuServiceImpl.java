package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.QueryWrapperDecorator;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.mapper.MenuMapper;
import com.myblog.service.security.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.security.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private static Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据角色获取对应菜单，用于渲染侧边栏
     * @param roles
     * @return
     */
    @Override
    public List<Menu> getMenuByRoles(List<Role> roles) {
        return menuMapper.getMenuByRoles(roles);
    }

    /**
     * 根据菜单id获取菜单信息
     * @param id
     * @return
     */
    @Override
    public MenuDto getMenuById(String id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            LOGGER.error("getMenuById:[{}] failed from db", id);
            return new MenuDto();
        }
        MenuDto menuDto = new MenuDto();
        BeanUtil.copyProperties(menu, menuDto);
        menuDto.setId(menu.getId());
        menuDto.setCreateTime(menu.getCreateTime());
        menuDto.setUpdateTime(menu.getUpdateTime());
        if (Objects.equals("Layout", menuDto.getComponent())) {
            menuDto.setComponent("");
        }
        return menuDto;
    }

    /**
     * 根据pid获取与当前菜单同级别的菜单信息
     * @param pid
     * @return
     */
    @Override
    public List<MenuDto> getMenusByPid(String pid) {
        if (StringUtils.isBlank(pid)) {
            pid = "0";
        }
        QueryWrapperDecorator<Menu> decorator = new QueryWrapperDecorator<>();
        QueryWrapper<Menu> wrapper = decorator.createBaseQueryWrapper();
        wrapper.eq(DbConstants.Base.pid, pid);
        return toDto(menuMapper.selectList(wrapper));
    }

    /**
     * 获取当前菜单和上级菜单信息
     * @param menuDto
     * @param menuDtos
     * @return
     */
    @Override
    public List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menuDtos) {
        // 如果是0，说明是最高级别的菜单了
        if (Objects.equals("0", menuDto.getPid())) {
            QueryWrapperDecorator<Menu> decorator = new QueryWrapperDecorator<>();
            QueryWrapper<Menu> wrapper = decorator.createBaseQueryWrapper();
            wrapper.eq(DbConstants.Base.pid,  "0");
            menuDtos.addAll(toDto(menuMapper.selectList(wrapper)));
            return menuDtos;
        }
        // 获取与当前菜单同级别的菜单信息
        menuDtos.addAll(getMenusByPid(menuDto.getPid()));
        // 获取当前菜单上一级别的菜单，并再次进入getSuperior方法来获取上级菜单同级别的所有菜单的信息，直至获取到所有菜单的信息
        return getSuperior(getMenuById(menuDto.getPid()), menuDtos);
    }

    private List<MenuDto> toDto(List<Menu> menus) {
        List<MenuDto> menuDtos = new ArrayList<>();
        for (Menu menu : menus) {
            MenuDto menuDto = new MenuDto();
            BeanUtil.copyProperties(menu, menuDto);
            menuDto.setId(menu.getId());
            menuDto.setCreateTime(menu.getCreateTime());
            menuDto.setUpdateTime(menu.getUpdateTime());
            if (Objects.equals("Layout", menuDto.getComponent())) {
                menuDto.setComponent("");
            }
            menuDtos.add(menuDto);
        }
        return menuDtos;
    }
}
