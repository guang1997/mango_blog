package com.myblog.service.admin.controller;


import com.myblog.service.base.common.ResultModel;
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

import java.util.Map;
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
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @LogByMethod("/admin/role/getRoleByPage")
    @ApiOperation(value = "分页查询角色", notes = "分页查询角色", response = Response.class)
    @PostMapping("/getRoleByPage")
    public ResultModel<Map<String, Object>> getRoleByPage(@RequestBody RoleDto roleDto) throws Exception {
        return ResultModel.ok(roleService.getRoleByPage(roleDto));
    }

    @LogByMethod("/admin/role/getRoleById")
    @ApiOperation(value = "根据id获取角色信息", notes = "根据id获取角色信息", response = Response.class)
    @GetMapping("/getRoleById")
    public ResultModel<RoleDto> getRoleById(String id) throws Exception {
        Role role = roleService.getById(id);
        return ResultModel.ok(roleService.getRoleById(role));
    }

    @LogByMethod(value = "/admin/role/addRole", validate = true)
    @ApiOperation(value = "新增角色", notes = "新增角色", response = Response.class)
    @PostMapping("/addRole")
    public ResultModel<Object> addRole(@RequestBody RoleDto roleDto) throws Exception {
        if (roleService.addRole(roleDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
    }

    @LogByMethod(value = "/admin/role/editRole", validate = true)
    @ApiOperation(value = "修改角色", notes = "修改角色", response = Response.class)
    @PutMapping("/editRole")
    public ResultModel<Object> editRole(@RequestBody RoleDto roleDto) throws Exception {
        if (roleService.editRole(roleDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
    }

    @LogByMethod("/admin/role/delRole")
    @ApiOperation(value = "删除角色", notes = "删除角色", response = Response.class)
    @DeleteMapping("/delRole")
    public ResultModel<Object> delRole(@RequestBody Set<String> ids) throws Exception {
        if (roleService.delRole(ids)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.DELETE_FAILED);
    }

    @LogByMethod("/admin/role/menu")
    @ApiOperation(value = "保存菜单", notes = "保存菜单", response = Response.class)
    @PostMapping("/menu")
    public ResultModel<Object> menu(@RequestBody RoleDto roleDto) throws Exception {
        if (StringUtils.isBlank(roleDto.getId())) {
            log.error("editMenu failed, roleId cannot be null, role:{}", roleDto);
            return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        if (roleService.updateMenu(roleDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
    }
}

