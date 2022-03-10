package com.myblog.service.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.util.BeanUtil;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.entity.vo.RoleVo;
import com.myblog.service.security.mapper.RoleMapper;
import com.myblog.service.security.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByUserName(String username) {
        return roleMapper.getRolesByUserName(username);
    }

    /**
     * 分页查询角色信息
     * @param roleVo
     * @return
     */
    @Override
    public Response getRoleByPage(RoleVo roleVo) {
        // 全量查询返回所有数据
        if (!Objects.equals(null, roleVo) && roleVo.getSearchAll()) {
            return Response.ok().data(Constants.ReplyField.DATA, toDto(roleMapper.selectList(null)));
        }
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
        roleMapper.selectPage(rolePage, queryWrapper);
        response.data(Constants.ReplyField.DATA, toDto(rolePage.getRecords()));
        response.data(Constants.ReplyField.TOTAL, rolePage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
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
