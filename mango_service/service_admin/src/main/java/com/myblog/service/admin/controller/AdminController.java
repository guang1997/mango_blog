package com.myblog.service.admin.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Set;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-26
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/manager")
@Api(value = "管理员相关接口", tags = {"管理员相关接口"})
public class AdminController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @LogByMethod("/admin/manager/getAdminByPage")
    @ApiOperation(value = "分页查询管理员信息", notes = "分页查询管理员信息", response = Response.class)
    @PostMapping("/getAdminByPage")
    public Response getAdminByPage(@RequestBody AdminDto adminDto) throws ParseException {
        Response response = Response.ok();
        try {
            response = adminService.getAdminByPage(adminDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/manager/addAdmin")
    @ApiOperation(value = "新增管理员", notes = "新增管理员", response = Response.class)
    @PostMapping("/addAdmin")
    public Response addAdmin(@RequestBody AdminDto adminDto) {
        Response response = Response.ok();
        try {
            response = adminService.addAdmin(adminDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/manager/editAdmin")
    @ApiOperation(value = "修改管理员", notes = "修改管理员", response = Response.class)
    @PutMapping("/editAdmin")
    public Response editRole(@RequestBody RoleDto roleDto) {
        Response response = Response.ok();
        try {
//            response = roleService.editRole(roleDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/manager/delAdmin")
    @ApiOperation(value = "删除管理员", notes = "删除管理员", response = Response.class)
    @DeleteMapping("/delAdmin")
    public Response delAdmin(@RequestBody Set<String> ids) {
        Response response = Response.ok();
        try {
            response = adminService.delAdmin(ids);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

}

