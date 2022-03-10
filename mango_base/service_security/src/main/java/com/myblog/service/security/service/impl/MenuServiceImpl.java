package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import org.springframework.util.CollectionUtils;

import java.util.*;

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
            throw new RuntimeException("cannot find menu from db");
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
        wrapper.eq(DbConstants.Base.PID, pid);
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
            wrapper.eq(DbConstants.Base.PID,  "0");
            menuDtos.addAll(toDto(menuMapper.selectList(wrapper)));
            return menuDtos;
        }
        // 获取与当前菜单同级别的菜单信息
        menuDtos.addAll(getMenusByPid(menuDto.getPid()));
        // 获取当前菜单上一级别的菜单，并再次进入getSuperior方法来获取上级菜单同级别的所有菜单的信息，直至获取到所有菜单的信息
        return getSuperior(getMenuById(menuDto.getPid()), menuDtos);
    }

    /**
     * 获取当前菜单和下级菜单信息
     * @param childrenList
     * @param menuDtos
     * @return
     */
    @Override
    public Set<MenuDto> getChildren(List<MenuDto> childrenList, Set<MenuDto> menuDtos) {
        for (MenuDto menuDto : childrenList) {
            menuDtos.add(menuDto);
            List<MenuDto> menusByPid = this.getMenusByPid(menuDto.getId());
            if (!CollectionUtils.isEmpty(menusByPid)) {
                getChildren(menusByPid, menuDtos);
            }

        }
        return menuDtos;
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @Override
    public Response addMenu(Menu menu) {
        // 校验菜单是否已经存在
        QueryWrapperDecorator<Menu> decorator = new QueryWrapperDecorator<>();
        QueryWrapper<Menu> queryWrapper = decorator.createBaseQueryWrapper();
        queryWrapper.eq(DbConstants.Menu.TITLE, menu.getTitle());
        if (!Objects.equals("Layout", menu.getComponent())) {
            queryWrapper.or().eq(DbConstants.Menu.COMPONENT, menu.getComponent());
        }
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(menus)) {
            LOGGER.error("addMenu failed, menu already exist in db, menu:{}", menu);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        if (StringUtils.isBlank(menu.getName())) {
            menu.setName(menu.getTitle());
        }
        if (menuMapper.updateByTitle(menu) < 1) {
            if (menuMapper.insert(menu) < 1) {
                LOGGER.error("addMenu failed, menu:{}", menu);
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
        }

        // 更新父菜单subCount
        if (Objects.equals("0", menu.getPid())) {
            return Response.ok();
        }
        QueryWrapper<Menu> countWrapper = decorator.createBaseQueryWrapper();
        countWrapper.eq(DbConstants.Base.PID, menu.getPid());
        Integer subCount = menuMapper.selectCount(countWrapper);
        if (menuMapper.updateSubCount(menu.getPid(), ++subCount) < 1) {
            LOGGER.error("addMenu failed by updateSubCount, menu:{}", menu);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @Override
    public Response editMenu(Menu menu) {
        // 获取旧的菜单信息，用于更新父菜单的subCount
        MenuDto oldMenu = this.getMenuById(menu.getId());
        if (menuMapper.updateById(menu) < 1) {
            LOGGER.error("editMenu failed, menu:{}", menu);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }

        if (!Objects.equals(oldMenu.getPid(), menu.getPid())) {
            Menu oldParentMenu = menuMapper.selectById(oldMenu.getPid());
            Menu newParentMenu = menuMapper.selectById(menu.getPid());
            menuMapper.updateSubCount(oldParentMenu.getId(), oldParentMenu.getSubCount() - 1);
            menuMapper.updateSubCount(newParentMenu.getId(), newParentMenu.getSubCount() + 1);
        }
        return Response.ok();
    }

    /**
     * 删除菜单
     * @param ids
     * @return
     */
    @Override
    public Response delMenu(List<String> ids) {
        if (menuMapper.deleteBatchIds(ids) < 1) {
            LOGGER.error("delMenu failed, ids:{}", ids);
            return Response.setResult(ResultCodeEnum.DELETE_FAILED);
        }
        return Response.ok();
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
