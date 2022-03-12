package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.entity.vo.RoleVo;
import com.myblog.service.security.mapper.RoleMapper;
import com.myblog.service.security.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    @Override
    public List<Role> getRolesByUserName(String username) {
        return baseMapper.getRolesByUserName(username);
    }

    /**
     * 分页查询角色信息
     * @param roleVo
     * @return
     */
    @Override
    public Response getRoleByPage(RoleVo roleVo) throws Exception {
        Response response = Response.ok();
        int page = 1;
        int size = 10;
        if (!Objects.isNull(roleVo.getPage())) page = roleVo.getPage();
        if (!Objects.isNull(roleVo.getSize())) size = roleVo.getSize();

        Page<Role> rolePage = new Page<>(page, size);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(roleVo.getBlurry())) {
            queryWrapper.like(DbConstants.Role.ROLE_NAME, roleVo.getBlurry());
            queryWrapper.or().like(DbConstants.Role.SUMMARY, roleVo.getBlurry());
        }
        if (!CollectionUtils.isEmpty(roleVo.getCreateTime()) && Objects.equals(2, roleVo.getCreateTime().size())) {
            Date beginDate = ThreadSafeDateFormat.parse(roleVo.getCreateTime().get(0), ThreadSafeDateFormat.DATETIME);
            Date endDate = ThreadSafeDateFormat.parse(roleVo.getCreateTime().get(1), ThreadSafeDateFormat.DATETIME);
            queryWrapper.between(DbConstants.Base.CREATE_TIME, beginDate, endDate);
        }
        baseMapper.selectPage(rolePage, queryWrapper);
        // 获取角色菜单信息
        List<RoleDto> roleDtos = toDto(rolePage.getRecords());
        for (RoleDto roleDto : roleDtos) {
            List<MenuDto> menuDtos = baseMapper.selectRoleMenu(roleDto.getId());
            roleDto.setMenus(menuDtos);
        }

        response.data(Constants.ReplyField.DATA, roleDtos);
        response.data(Constants.ReplyField.TOTAL, rolePage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    /**
     * 保存角色信息
     * @param role
     * @return
     */
    @Override
    public Response addRole(Role role) {
        // 校验管理员是否已经存在
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Role.ROLE_NAME, role.getRoleName());
        List<Role> menus = baseMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(menus)) {
            LOGGER.error("addRole failed, role already exist in db, role:{}", role);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
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
            baseMapper.batchDeleteRoleAdminByRoleId(delSuccessedRoleIds);
        }
        if (CollectionUtils.isEmpty(delFailedRoleNames)) {
            return Response.ok();
        }
        return Response.error().message(delFailedRoleNames.toString() + "已绑定用户, 未删除成功");
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @Override
    public Response editRole(Role role) {
        if (baseMapper.updateById(role) < 1) {
            LOGGER.error("editRole failed, role:{}", role);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
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
