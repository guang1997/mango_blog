package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.entity.vo.BaseVO;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.mapper.MenuMapper;
import com.myblog.service.security.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.security.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq(DbConstants.Base.isDeleted, 0);
        wrapper.orderByAsc(DbConstants.Base.sort);
        List<Menu> menus = menuMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(menus)) {
            return Response.setResult(ResultCodeEnum.QUERY_FAILED);
        }
        List<Menu> resultMenu = TreeUtil.toMenuTree(menus, "1");
        return Response.ok().data(Constants.ReplyField.DATA, resultMenu);
    }
}
