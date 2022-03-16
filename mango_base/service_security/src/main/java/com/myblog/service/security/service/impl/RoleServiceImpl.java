package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BaseUtil;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.RoleAdmin;
import com.myblog.service.security.entity.RoleMenu;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.mapper.RoleAdminMapper;
import com.myblog.service.security.mapper.RoleMapper;
import com.myblog.service.security.mapper.RoleMenuMapper;
import com.myblog.service.security.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private static Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleAdminMapper roleAdminMapper;

    @Override
    public List<Role> getRolesByUserId(String userId) {
        return baseMapper.getRolesByUserId(userId);
    }

    /**
     * 分页查询角色信息
     * @param roleDto
     * @return
     */
    @Override
    public Response getRoleByPage(RoleDto roleDto) throws Exception {
        Response response = Response.ok();
        if (!Objects.isNull(roleDto.getSearchAll()) && roleDto.getSearchAll()) {
            QueryWrapper<Role> allQueryWrapper = new QueryWrapper<>();
            return response.data(Constants.ReplyField.DATA, toDto(baseMapper.selectList(allQueryWrapper)));
        }
        int page = 1;
        int size = 10;
        if (!Objects.isNull(roleDto.getPage())) page = roleDto.getPage();
        if (!Objects.isNull(roleDto.getSize())) size = roleDto.getSize();

        Page<Role> rolePage = new Page<>(page, size);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(roleDto.getBlurry())) {
            queryWrapper.like(DbConstants.Role.ROLE_NAME, roleDto.getBlurry());
            queryWrapper.or().like(DbConstants.Role.SUMMARY, roleDto.getBlurry());
        }
        if (!CollectionUtils.isEmpty(roleDto.getCreateTimes()) && Objects.equals(2, roleDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(roleDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(roleDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        baseMapper.selectPage(rolePage, queryWrapper);
        // 获取角色菜单信息
        List<RoleDto> roleDtos = toDto(rolePage.getRecords());
        for (RoleDto dto : roleDtos) {
            List<MenuDto> menuDtos = baseMapper.selectRoleMenu(dto.getId());
            dto.setMenus(menuDtos);
        }

        response.data(Constants.ReplyField.DATA, roleDtos);
        response.data(Constants.ReplyField.TOTAL, rolePage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 保存角色信息
     * @param roleDto
     * @return
     */
    @Override
    public Response addRole(RoleDto roleDto) {
        // 校验管理员是否已经存在
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Role.ROLE_NAME, roleDto.getRoleName());
        List<Role> menus = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(menus)) {
            LOGGER.error("addRole failed, role already exist in db, role:{}", roleDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        Role role = toRole(roleDto);
        if (baseMapper.insert(role) < 1) {
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return Response.ok();
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    @Override
    public Response delRole(Set<String> ids) {
        // 如果角色已经绑定了用户，那么不删除该角色
        List<String> delFailedRoleNames = new ArrayList<>();
        List<String> delSuccessedRoleIds = new ArrayList<>();
        for (String id : ids) {
            List<String> adminIdsByRoleId = baseMapper.getAdminIdsByRoleId(id);
            if (!CollectionUtils.isEmpty(adminIdsByRoleId)) {
                Role role = baseMapper.selectById(id);
                LOGGER.warn("delete role failed, the role:{} has been bound", role);
                delFailedRoleNames.add(role.getRoleName());
                continue;
            }
            if (baseMapper.deleteById(id) < 1) {
                return Response.setResult(ResultCodeEnum.SAVE_FAILED);
            }
            delSuccessedRoleIds.add(id);
        }
        if (!CollectionUtils.isEmpty(delSuccessedRoleIds)) {
            QueryWrapper<RoleAdmin> roleMenuQueryWrapper = new QueryWrapper<>();
            roleMenuQueryWrapper.in(DbConstants.Base.ROLE_ID, delSuccessedRoleIds);
            roleAdminMapper.delete(roleMenuQueryWrapper);
        }
        if (CollectionUtils.isEmpty(delFailedRoleNames)) {
            return Response.ok();
        }
        return Response.error().message(delFailedRoleNames.toString() + "已绑定用户, 未删除成功");
    }

    /**
     * 修改角色
     * @param roleDto
     * @return
     */
    @Override
    public Response editRole(RoleDto roleDto) {
        Role role = toRole(roleDto);
        if (baseMapper.updateById(role) < 1) {
            LOGGER.error("editRole failed, role:{}", role);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
    }

    private Role toRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDto, role);
        if (StringUtils.isNotBlank(roleDto.getId())) {
            role.setId(roleDto.getId());
        }
        return role;
    }

    /**
     * 给角色授权菜单
     * @param roleDto
     * @return
     */
    @Override
    public Response updateMenu(RoleDto roleDto) {
        // 先删掉该角色绑定的所有菜单的信息
        baseMapper.deleteRoleMenuByRoleId(roleDto.getId());

        // 再将菜单绑定给角色
        List<RoleMenu> roleMenus = roleDto.getMenus().stream()
                .filter(BaseUtil.distinctByKey(MenuDto::getId))
                .map(dto -> toRoleMenu(dto.getId(), roleDto.getId()))
                .collect(Collectors.toList());

        for (RoleMenu roleMenu : roleMenus) {
            if (roleMenuMapper.insert(roleMenu) < 1) {
                LOGGER.error("updateMenu failed, role:{}, roleMenus:{}", roleDto, roleMenus);
                return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
            }
        }
        return Response.ok();
    }

    /**
     * 根据id获取角色信息，包含菜单相关信息
     * @param role
     * @return
     */
    @Override
    public Response getRoleById(Role role) {
        RoleDto roleDto = new RoleDto();
        BeanUtil.copyProperties(role, roleDto);
        roleDto.setId(role.getId());
        roleDto.setCreateTime(role.getCreateTime());
        roleDto.setMenus(baseMapper.selectRoleMenu(roleDto.getId()));
        return Response.ok().data(Constants.ReplyField.DATA, roleDto);
    }

    private RoleMenu toRoleMenu(String menuId, String roleId) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setMenuId(menuId);
        roleMenu.setRoleId(roleId);
        return roleMenu;
    }
    private List<RoleDto> toDto(List<Role> roleList) {
        List<RoleDto> roleDtos = new ArrayList<>();
        for (Role role : roleList) {
            RoleDto roleDto = new RoleDto();
            BeanUtil.copyProperties(role, roleDto);
            roleDto.setId(role.getId());
            roleDto.setCreateTime(role.getCreateTime());
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

}
