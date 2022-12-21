package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.mapper.MenuMapper;
import com.myblog.service.security.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * 根据角色获取对应菜单，用于渲染侧边栏
     *
     * @param roles
     * @return
     */
    @Override
    public List<Menu> getMenuByRoles(List<Role> roles) {
        return baseMapper.getMenuByRoles(roles);
    }

    /**
     * 根据菜单id获取菜单信息
     *
     * @param id
     * @return
     */
    @Override
    public MenuDto getMenuById(String id) throws Exception {
        Menu menu = baseMapper.selectById(id);
        if (menu == null) {
            LOGGER.error("getMenuById:[{}] failed from db", id);
            throw new RuntimeException("cannot find menu from db");
        }
        return this.toDto(menu, MenuDto.class);
    }

    /**
     * 根据pid获取与当前菜单同级别的菜单信息
     *
     * @param pid
     * @return
     */
    @Override
    public List<MenuDto> getMenusByPid(String pid) throws Exception {
        if (StringUtils.isBlank(pid)) {
            pid = "0";
        }
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        wrapper.orderByDesc(DbConstants.Base.SORT);
        wrapper.eq(DbConstants.Base.PID, pid);
        return this.toDtoList(baseMapper.selectList(wrapper), MenuDto.class);
    }

    /**
     * 获取当前菜单和上级菜单信息
     *
     * @param menuDto
     * @param menuDtos
     * @return
     */
    @Override
    public List<MenuDto> getSuperior(MenuDto menuDto, List<MenuDto> menuDtos) throws Exception {
        // 如果是0，说明是最高级别的菜单了
        if (Objects.equals("0", menuDto.getPid())) {
            QueryWrapper<Menu> wrapper = new QueryWrapper<>();
            wrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
            wrapper.orderByDesc(DbConstants.Base.SORT);
            wrapper.eq(DbConstants.Base.PID, "0");
            menuDtos.addAll(toDtoList(baseMapper.selectList(wrapper), MenuDto.class));
            return menuDtos;
        }
        // 获取与当前菜单同级别的菜单信息
        menuDtos.addAll(getMenusByPid(menuDto.getPid()));
        // 获取当前菜单上一级别的菜单，并再次进入getSuperior方法来获取上级菜单同级别的所有菜单的信息，直至获取到所有菜单的信息
        return getSuperior(getMenuById(menuDto.getPid()), menuDtos);
    }

    /**
     * 获取当前菜单和下级菜单信息
     *
     * @param childrenList
     * @param menuDtos
     * @return
     */
    @Override
    public Set<MenuDto> getChildren(List<MenuDto> childrenList, Set<MenuDto> menuDtos) throws Exception {
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
     *
     * @param menuDto
     * @return
     */
    @Override
    public Response addMenu(MenuDto menuDto) throws Exception {
        // 校验菜单是否已经存在
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        queryWrapper.orderByDesc(DbConstants.Base.SORT);
        queryWrapper.eq(DbConstants.Menu.TITLE, menuDto.getTitle());
        queryWrapper.eq(DbConstants.Menu.PID, menuDto.getPid());
        if (!Objects.equals("Layout", menuDto.getComponent())) {
            queryWrapper.or().eq(DbConstants.Menu.COMPONENT, menuDto.getComponent());
        }
        List<Menu> menus = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(menus)) {
            LOGGER.error("addMenu failed, menu already exist in db, menu:{}", menuDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        if (StringUtils.isBlank(menuDto.getName())) {
            menuDto.setName(menuDto.getTitle());
        }
        Menu menu = this.toDb(menuDto, Menu.class);
        // 如果已经有同名且被删除的菜单，那么只更新
        menu.setCreateTime(new Date());
        menu.setUpdateTime(new Date());
        if (baseMapper.updateByTitle(menu) < 1) {
            if (baseMapper.insert(menu) < 1) {
                LOGGER.error("addMenu failed by unknown error, menu:{}", menu);
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
        }

        // 更新父菜单subCount
        if (Objects.equals("0", menu.getPid())) {
            return Response.ok();
        }
        QueryWrapper<Menu> countWrapper = new QueryWrapper<>();
        countWrapper.eq(DbConstants.Base.IS_DELETED, Constants.IsDeleted.NO);
        countWrapper.eq(DbConstants.Base.PID, menu.getPid());
        Integer subCount = baseMapper.selectCount(countWrapper);
        if (baseMapper.updateSubCount(menu.getPid(), ++subCount) < 1) {
            LOGGER.error("addMenu failed by updateSubCount, menu:{}", menu);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 更新菜单
     *
     * @param menuDto
     * @return
     */
    @Override
    public Response editMenu(MenuDto menuDto) throws Exception {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Base.PID, menuDto.getPid());
        List<Menu> menus = baseMapper.selectList(queryWrapper);
        if (menus.size() > 0) {
            for (Menu menu : menus) {
                if (Objects.equals(menu.getTitle(), menuDto.getTitle()) && !Objects.equals(menu.getId(), menuDto.getId())) {
                    return Response.error().message("更新失败, 该父菜单下已存在相同名称的菜单");
                }
            }
        }
        // 获取旧的菜单信息，用于更新父菜单的subCount
        MenuDto oldMenu = this.getMenuById(menuDto.getId());
        Menu menu = this.toDb(menuDto, Menu.class);
        if (baseMapper.updateById(menu) < 1) {
            LOGGER.error("editMenu failed, menu:{}", menu);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }

        if (!Objects.equals(oldMenu.getPid(), menu.getPid())) {
            Menu oldParentMenu = baseMapper.selectById(oldMenu.getPid());
            Menu newParentMenu = baseMapper.selectById(menu.getPid());
            baseMapper.updateSubCount(oldParentMenu.getId(), oldParentMenu.getSubCount() - 1);
            baseMapper.updateSubCount(newParentMenu.getId(), newParentMenu.getSubCount() + 1);
        }
        return Response.ok();
    }

    /**
     * 删除菜单
     *
     * @param ids
     * @return
     */
    @Override
    public Response delMenu(List<String> ids) throws Exception {
        if (baseMapper.deleteBatchIds(ids) < 1) {
            LOGGER.error("delMenu failed by unknown error, ids:{}", ids);
            return Response.setResult(ResultCodeEnum.DELETE_FAILED);
        }
        // 清除角色和菜单中间表
        baseMapper.batchDeleteRoleMenuByMenuId(ids);
        return Response.ok();
    }

    @Override
    public void setDtoExtraProperties(Menu menu, MenuDto menuDto) {
        menuDto.setId(menu.getId());
        menuDto.setCreateTime(menu.getCreateTime());
        menuDto.setUpdateTime(menu.getUpdateTime());
        if (Objects.equals("Layout", menuDto.getComponent())) {
            menuDto.setComponent("");
        }
    }
}
