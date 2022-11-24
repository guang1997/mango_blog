package com.myblog.service.admin.controller;


import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.service.RoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @LogByMethod("/admin/role/getRoleByPage")
    @ApiOperation(value = "分页查询角色", notes = "分页查询角色", response = Response.class)
    @PostMapping("/getRoleByPage")
    public Response getRoleByPage(@RequestBody RoleDto roleDto) throws Exception {
        return roleService.getRoleByPage(roleDto);
    }

    @LogByMethod("/admin/role/getRoleById")
    @ApiOperation(value = "根据id获取角色信息", notes = "根据id获取角色信息", response = Response.class)
    @GetMapping("/getRoleById")
    public Response getRoleById(String id) throws Exception {
        Role role = roleService.getById(id);
        return roleService.getRoleById(role);
    }

    @LogByMethod(value = "/admin/role/addRole", validate = true)
    @ApiOperation(value = "新增角色", notes = "新增角色", response = Response.class)
    @PostMapping("/addRole")
    public Response addRole(@RequestBody RoleDto roleDto) throws Exception {
        return roleService.addRole(roleDto);
    }

    @LogByMethod(value = "/admin/role/editRole", validate = true)
    @ApiOperation(value = "修改角色", notes = "修改角色", response = Response.class)
    @PutMapping("/editRole")
    public Response editRole(@RequestBody RoleDto roleDto) throws Exception {
        return roleService.editRole(roleDto);
    }

    @LogByMethod("/admin/role/delRole")
    @ApiOperation(value = "删除角色", notes = "删除角色", response = Response.class)
    @DeleteMapping("/delRole")
    public Response delRole(@RequestBody Set<String> ids) throws Exception {
        return roleService.delRole(ids);
    }

    @LogByMethod("/admin/role/menu")
    @ApiOperation(value = "保存菜单", notes = "保存菜单", response = Response.class)
    @PostMapping("/menu")
    public Response menu(@RequestBody RoleDto roleDto) throws Exception {
        if (StringUtils.isBlank(roleDto.getId())) {
            log.error("editMenu failed, roleId cannot be null, role:{}", roleDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return roleService.updateMenu(roleDto);
    }
}

