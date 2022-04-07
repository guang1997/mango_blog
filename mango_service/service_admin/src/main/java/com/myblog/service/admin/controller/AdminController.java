package com.myblog.service.admin.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.PassAndEmailDto;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Autowired
    private RoleService roleService;

    @LogByMethod("/admin/manager/getAdminByPage")
    @ApiOperation(value = "分页查询管理员信息", notes = "分页查询管理员信息", response = Response.class)
    @PostMapping("/getAdminByPage")
    public Response getAdminByPage(@RequestBody AdminDto adminDto) throws Exception {
        return adminService.getAdminByPage(adminDto);
    }

    @LogByMethod("/admin/manager/addAdmin")
    @ApiOperation(value = "新增管理员", notes = "新增管理员", response = Response.class)
    @PostMapping("/addAdmin")
    public Response addAdmin(@RequestBody AdminDto adminDto) throws Exception {
        if (StringUtils.isBlank(adminDto.getUsername())
                || StringUtils.isBlank(adminDto.getEmail())
                || StringUtils.isBlank(adminDto.getPhone())
                || StringUtils.isBlank(adminDto.getNickname())) {
            LOGGER.error("addAdmin failed, username or email or phone or nickname cannot be null, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        List<RoleDto> roles = adminDto.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            for (RoleDto roleDto : roles) {
                Role role = roleService.getById(roleDto.getId());
                roleService.validRoleLevel(role.getLevel(), role.getRoleName());
            }
        }
        return adminService.addAdmin(adminDto);
    }

    @LogByMethod("/admin/manager/editAdmin")
    @ApiOperation(value = "修改管理员", notes = "修改管理员", response = Response.class)
    @PutMapping("/editAdmin")
    public Response editAdmin(@RequestBody AdminDto adminDto) {
        roleService.validRoleLevelByUserId(adminDto.getId());
        Response response = Response.ok();
        try {
//            response = roleService.editRole(roleDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/manager/editAdminFromCenter")
    @ApiOperation(value = "从个人中心页面修改管理员", notes = "从个人中心页面修改管理员", response = Response.class)
    @PutMapping("/editAdminFromCenter")
    public Response editAdminFromCenter(@RequestBody AdminDto adminDto) throws Exception {
        if (StringUtils.isBlank(adminDto.getQqNumber())
                || StringUtils.isBlank(adminDto.getWeChat())) {
            LOGGER.error("editAdminFromCenter failed, qqNumber or weChat cannot be null, admin:{}", adminDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return adminService.editAdminFromCenter(adminDto);
    }

    @LogByMethod("/admin/manager/updatePass")
    @ApiOperation(value = "从个人中心页面修改密码", notes = "从个人中心页面修改密码", response = Response.class)
    @PostMapping("/updatePass")
    @Transactional(rollbackFor = Exception.class)
    public Response updatePass(@RequestBody PassAndEmailDto passAndEmailDto) throws Exception {
        if (StringUtils.isBlank(passAndEmailDto.getOldPass())
                || StringUtils.isBlank(passAndEmailDto.getNewPass())) {
            LOGGER.error("updatePass failed, pass cannot be null");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return adminService.updatePass(passAndEmailDto);

    }

    @LogByMethod("/admin/manager/updateEmail")
    @ApiOperation(value = "从个人中心页面修改邮箱", notes = "从个人中心页面修改邮箱", response = Response.class)
    @PostMapping("/updateEmail")
    @Transactional(rollbackFor = Exception.class)
    public Response updateEmail(@RequestBody PassAndEmailDto passAndEmailDto) throws Exception {
        if (StringUtils.isBlank(passAndEmailDto.getPass())
                || StringUtils.isBlank(passAndEmailDto.getEmail())) {
            LOGGER.error("updateEmail failed, pass or email cannot be null");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return adminService.updateEmail(passAndEmailDto);

    }

    @LogByMethod("/admin/manager/delAdmin")
    @ApiOperation(value = "删除管理员", notes = "删除管理员", response = Response.class)
    @DeleteMapping("/delAdmin")
    public Response delAdmin(@RequestBody Set<String> ids) throws Exception {
        for (String id : ids) {

        }
        return adminService.delAdmin(ids);
    }

}

