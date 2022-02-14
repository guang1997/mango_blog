package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.entity.vo.BaseVO;
import com.myblog.service.base.util.QueryWrapperDecorator;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.vo.MenuVo;
import com.myblog.service.security.mapper.MenuMapper;
import com.myblog.service.security.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.security.util.TreeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
     * 获取所有的菜单，用于展示菜单列表
     * @return
     */
    @Override
    public Response getAllMenu() {
        QueryWrapperDecorator<Menu> decorator = new QueryWrapperDecorator<>();
        QueryWrapper<Menu> wrapper = decorator.createBaseQueryWrapper();
        List<Menu> menus = menuMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(menus)) {
            return Response.setResult(ResultCodeEnum.QUERY_FAILED);
        }
        List<Menu> resultMenu = TreeUtil.toMenuTree(menus, "0");
        return Response.ok().data(Constants.ReplyField.DATA, resultMenu);
    }

    /**
     * 根据菜单id获取菜单信息和上级菜单信息
     * @param id
     * @return
     */
    @Override
    public Response getMenuById(String id) {
        Menu menu = baseMapper.selectById(id);
        if (menu == null) {
            LOGGER.error("getMenuById:[{}] failed from db", id);
            return Response.setResult(ResultCodeEnum.QUERY_FAILED);
        }
        List<Menu> menus = new ArrayList<>();
        menus.add(menu);
        String pid = menu.getPid();
        if (!"0".equals(pid)) {
            QueryWrapperDecorator<Menu> decorator = new QueryWrapperDecorator<>();
            QueryWrapper<Menu> wrapper = decorator.createBaseQueryWrapper();
            wrapper.eq(DbConstants.Base.id, menu.getPid());
            Menu parentMenu = baseMapper.selectOne(wrapper);
            pid = parentMenu.getPid();
            menus.add(parentMenu);
        }
        List<Menu> resultMenus = TreeUtil.toMenuTree(menus, pid);
        return Response.ok().data(Constants.ReplyField.DATA, resultMenus);
    }

    /**
     * 根据pid获取当前菜单及下级菜单信息
     * @param pid
     * @return
     */
    @Override
    public Response getMenusByPid(String pid) {
        List<Menu> menus = new ArrayList<>();
        if (StringUtils.isEmpty(pid)) {
            pid = "0";
        }
        String treePid = pid;
        if (!"0".equals(pid)) {
            Menu menu = baseMapper.selectById(pid);
            if (menu == null) {
                LOGGER.error("getMenusByPid:[{}] failed from db", pid);
                return Response.setResult(ResultCodeEnum.QUERY_FAILED);
            }
            treePid = menu.getPid();
            menus.add(menu);
        }
        QueryWrapperDecorator<Menu> decorator = new QueryWrapperDecorator<>();
        QueryWrapper<Menu> wrapper = decorator.createBaseQueryWrapper();
        wrapper.eq(DbConstants.Base.pid, pid);
        menus.addAll(baseMapper.selectList(wrapper));
        List<Menu> resultMenus = TreeUtil.toMenuTree(menus, treePid);
        return Response.ok().data(Constants.ReplyField.DATA, resultMenus);
    }

}
