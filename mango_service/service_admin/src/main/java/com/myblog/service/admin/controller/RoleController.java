package com.myblog.service.admin.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.vo.RoleVo;
import com.myblog.service.security.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/role")
public class RoleController {


    private static Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @LogByMethod("/admin/role/getAllRole")
    @ApiOperation(value = "分页查询角色", notes = "分页查询角色", response = Response.class)
    @PostMapping("/getAllRole")
    public Response getAllRole(@RequestBody RoleVo roleVo) throws Exception {
        Response response = Response.ok();
        try {
            response = roleService.getRoleByPage(roleVo);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/role/addRole")
    @ApiOperation(value = "新增角色", notes = "新增角色", response = Response.class)
    @PostMapping("/addRole")
    public Response addRole(@RequestBody Role role) {
        Response response = Response.ok();
        try {
            if (StringUtils.isBlank(role.getRoleName())) {
                LOGGER.error("addMenu failed, roleName cannot be null, role:{}", role);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            response = roleService.addRole(role);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/role/editRole")
    @ApiOperation(value = "修改角色", notes = "修改角色", response = Response.class)
    @PutMapping("/editRole")
    public Response editRole(@RequestBody Role role) {
        Response response = Response.ok();
        try {
            response = roleService.editRole(role);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/role/delRole")
    @ApiOperation(value = "删除角色", notes = "删除角色", response = Response.class)
    @DeleteMapping("/delRole")
    public Response delRole(@RequestBody Set<String> ids) {
        Response response = Response.ok();
        try {
            response = roleService.delRole(ids);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }
}

