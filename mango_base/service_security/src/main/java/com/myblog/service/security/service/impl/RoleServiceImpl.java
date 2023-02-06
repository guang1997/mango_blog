package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.exception.BusinessException;
import com.myblog.service.base.util.BaseUtil;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.config.util.SecurityUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Map<String, Object> getRoleByPage(RoleDto roleDto) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (Objects.nonNull(roleDto.getSearchAll()) && roleDto.getSearchAll()) {
            QueryWrapper<Role> allQueryWrapper = new QueryWrapper<>();
            resultMap.put(Constants.ReplyField.DATA, this.toDtoList(baseMapper.selectList(allQueryWrapper), RoleDto.class));
            return resultMap;
        }

        Page<Role> rolePage = new Page<>(roleDto.getPage(), roleDto.getSize());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(roleDto.getBlurry())) {
            queryWrapper.like(DbConstants.Role.ROLE_NAME, roleDto.getBlurry());
            queryWrapper.or().like(DbConstants.Base.SUMMARY, roleDto.getBlurry());
        }
        if (!CollectionUtils.isEmpty(roleDto.getCreateTimes()) && Objects.equals(2, roleDto.getCreateTimes().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(roleDto.getCreateTimes().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(roleDto.getCreateTimes().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        baseMapper.selectPage(rolePage, queryWrapper);
        // 获取角色菜单信息
        List<RoleDto> roleDtos = this.toDtoList(rolePage.getRecords(), RoleDto.class);
        for (RoleDto dto : roleDtos) {
            List<MenuDto> menuDtos = baseMapper.selectRoleMenu(dto.getId());
            dto.setMenus(menuDtos);
        }

        resultMap.put(Constants.ReplyField.DATA, roleDtos);
        resultMap.put(Constants.ReplyField.TOTAL, rolePage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, roleDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, roleDto.getSize());
        return resultMap;
    }

    /**
     * 保存角色信息
     * @param roleDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addRole(RoleDto roleDto) throws Exception{
        // 校验角色是否已经存在
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Role.ROLE_NAME, roleDto.getRoleName());
        List<Role> roles = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(roles)) {
            LOGGER.error("addRole failed, role already exist in db, role:{}", roleDto);
            return false;
        }
        Role role = this.toDb(roleDto, Role.class);
        if (baseMapper.insert(role) < 1) {
            LOGGER.error("addRole failed by unknown error, role:{}", role);
            return false;
        }
        return true;
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delRole(Set<String> ids) {
        // 如果角色已经绑定了用户，那么不删除该角色
        List<String> delFailedRoleNames = new ArrayList<>();
        List<String> delSuccessedRoleIds = new ArrayList<>();
        for (String id : ids) {
            Role deleteRole = baseMapper.selectById(id);
            if (!validRoleLevel(deleteRole.getLevel(), deleteRole.getRoleName())) {
                return false;
            }
            List<String> adminIdsByRoleId = baseMapper.getAdminIdsByRoleId(id);
            if (!CollectionUtils.isEmpty(adminIdsByRoleId)) {
                Role role = baseMapper.selectById(id);
                LOGGER.warn("delete role failed, the role:{} has been bound", role);
                delFailedRoleNames.add(role.getRoleName());
                continue;
            }
            if (baseMapper.deleteById(id) < 1) {
                LOGGER.error("delRole failed by unknown error, roleId:{}", id);
                return false;
            }
            delSuccessedRoleIds.add(id);
        }
        if (!CollectionUtils.isEmpty(delSuccessedRoleIds)) {
            QueryWrapper<RoleAdmin> roleMenuQueryWrapper = new QueryWrapper<>();
            roleMenuQueryWrapper.in(DbConstants.Base.ROLE_ID, delSuccessedRoleIds);
            roleAdminMapper.delete(roleMenuQueryWrapper);
        }
        if (CollectionUtils.isEmpty(delFailedRoleNames)) {
            return true;
        }
        throw new BusinessException(delFailedRoleNames.toString() + "已绑定用户, 未删除成功");
    }

    /**
     * 修改角色
     * @param roleDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editRole(RoleDto roleDto) throws Exception{
        Role dbRole = baseMapper.selectById(roleDto.getId());
        if (!validRoleLevel(dbRole.getLevel(), dbRole.getRoleName())) {
            return false;
        }
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Role.ROLE_NAME, roleDto.getRoleName());
        List<Role> roles = baseMapper.selectList(queryWrapper);
        if (roles.size() > 0) {
            for (Role role : roles) {
                if (!Objects.equals(role.getId(), roleDto.getId())) {
                    throw new BusinessException("更新失败, 已存在相同名称的角色");
                }
            }
        }
        Role role = this.toDb(roleDto, Role.class);
        if (baseMapper.updateById(role) < 1) {
            LOGGER.error("editRole failed by unknown error, role:{}", role);
            return false;
        }
        return true;
    }

    /**
     * 给角色授权菜单
     * @param roleDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMenu(RoleDto roleDto) throws Exception{
        Role dbRole = baseMapper.selectById(roleDto.getId());
        if (!validRoleLevel(dbRole.getLevel(), dbRole.getRoleName())) {
            return false;
        }

        // 先删掉该角色绑定的所有菜单的信息
        baseMapper.deleteRoleMenuByRoleId(roleDto.getId());

        // 再将菜单绑定给角色
        List<RoleMenu> roleMenus = roleDto.getMenus().stream()
                .filter(BaseUtil.distinctByKey(MenuDto::getId))
                .map(dto -> toRoleMenu(dto.getId(), roleDto.getId()))
                .collect(Collectors.toList());

        for (RoleMenu roleMenu : roleMenus) {
            if (roleMenuMapper.insert(roleMenu) < 1) {
                LOGGER.error("updateMenu failed by unknown error, role:{}, roleMenus:{}", roleDto, roleMenu);
                return false;
            }
        }
        return true;
    }

    /**
     * 根据id获取角色信息，包含菜单相关信息
     * @param role
     * @return
     */
    @Override
    public RoleDto getRoleById(Role role) throws Exception{
        RoleDto roleDto = this.toDto(role, RoleDto.class);
        roleDto.setMenus(baseMapper.selectRoleMenu(roleDto.getId()));
        return roleDto;
    }

    private RoleMenu toRoleMenu(String menuId, String roleId) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setMenuId(menuId);
        roleMenu.setRoleId(roleId);
        return roleMenu;
    }

    /**
     * 校验角色级别是否大于被操作角色的级别，数字越小，角色权限越大
     * @param level
     * @return
     */
    @Override
    public Boolean validRoleLevel(Integer level, String roleName) {
        if (Objects.isNull(level)) {
            throw new BusinessException("找不到被操作角色的级别");
        }
        List<Role> roles = baseMapper.getRolesByUserId(SecurityUtils.getCurrentUserId());
        if (CollectionUtils.isEmpty(roles)) {
            throw new BusinessException("当前用户未绑定角色");
        }
        Set<String> roleNames = roles.stream().map(Role::getRoleName).collect(Collectors.toSet());
        // 如果所绑定的角色与被操作的角色名称相同，那么可以对其进行操作
        if (roleNames.contains(roleName)) {
            return true;
        }
        int currentLevel = roles.stream().mapToInt(Role::getLevel).min().getAsInt();
        if (currentLevel > level) {
            throw new BusinessException("权限不足，你的角色级别：" + currentLevel + "，低于操作的角色级别：" + level);
        }
        return true;
    }

    /**
     * 获取角色绑定的菜单信息
     * @param roleIds
     * @return
     */
    @Override
    public List<String> getMenusByRoleId(List<String> roleIds) {
        return baseMapper.selectRoleMenuButtons(roleIds);
    }

    @Override
    public Boolean validRoleLevelByUserId(String userId) {
        List<Role> roles = baseMapper.getRolesByUserId(userId);
        Role role = roles.stream().min(Comparator.comparingInt(Role::getLevel)).orElse(null);
        if (Objects.isNull(role)) {
            throw new RuntimeException("当前用户未绑定角色");
        }
        return validRoleLevel(role.getLevel(), role.getRoleName());
    }
}
