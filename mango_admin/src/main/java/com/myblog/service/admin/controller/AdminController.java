package com.myblog.service.admin.controller;


import com.myblog.service.admin.config.QiNiuYunOssProperties;
import com.myblog.service.admin.service.OssService;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.Role;
import com.myblog.service.security.entity.dto.AdminDto;
import com.myblog.service.security.entity.dto.AdminDto.AdminGroup;
import com.myblog.service.security.entity.dto.PassAndEmailDto;
import com.myblog.service.security.entity.dto.PassAndEmailDto.PassAndEmailGroup;
import com.myblog.service.security.entity.dto.RoleDto;
import com.myblog.service.security.service.AdminService;
import com.myblog.service.security.service.RoleService;
import com.myblog.service.security.service.VerificationCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-26
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/manager")
@Api(value = "管理员相关接口", tags = {"管理员相关接口"})
public class AdminController {


    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private QiNiuYunOssProperties qiNiuYunOssProperties;

    @Autowired
    private OssService ossService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @LogByMethod("/admin/manager/getAdminByPage")
    @ApiOperation(value = "分页查询管理员信息", notes = "分页查询管理员信息", response = Response.class)
    @PostMapping("/getAdminByPage")
    public ResultModel<Map<String, Object>> getAdminByPage(@RequestBody AdminDto adminDto) throws Exception {
        return ResultModel.ok(adminService.getAdminByPage(adminDto));
    }

    @LogByMethod("/admin/manager/addAdmin")
    @ApiOperation(value = "新增管理员", notes = "新增管理员", response = Response.class)
    @PostMapping("/addAdmin")
    public ResultModel<Object> addAdmin(@RequestBody @Validated(AdminGroup.AddAdmin.class) AdminDto adminDto) throws Exception {

        List<RoleDto> roles = adminDto.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            for (RoleDto roleDto : roles) {
                Role role = roleService.getById(roleDto.getId());
                if (!roleService.validRoleLevel(role.getLevel(), role.getRoleName())) {
                    return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
                }
            }
        }
        if (adminService.addAdmin(adminDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
    }

    @LogByMethod("/admin/manager/editAdmin")
    @ApiOperation(value = "修改管理员", notes = "修改管理员", response = Response.class)
    @PutMapping("/editAdmin")
    public ResultModel<Object> editAdmin(@RequestBody @Validated(AdminGroup.EditAdmin.class) AdminDto adminDto) throws Exception {
        if (!roleService.validRoleLevelByUserId(adminDto.getId())) {
            return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        if (adminService.editAdmin(adminDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
    }

    @LogByMethod("/admin/manager/editAdminFromCenter")
    @ApiOperation(value = "从个人中心页面修改管理员", notes = "从个人中心页面修改管理员", response = Response.class)
    @PutMapping("/editAdminFromCenter")
    public ResultModel<Object> editAdminFromCenter(@RequestBody @Validated(AdminGroup.EditAdminFromCenter.class) AdminDto adminDto) throws Exception {
        if (adminService.editAdminFromCenter(adminDto)) {
            return ResultModel.ok();
        }
        return ResultModel.error();
    }

    @LogByMethod("/admin/manager/updatePass")
    @ApiOperation(value = "从个人中心页面修改密码", notes = "从个人中心页面修改密码", response = Response.class)
    @PostMapping("/updatePass")
    @Transactional(rollbackFor = Exception.class)
    public ResultModel<Object> updatePass(@RequestBody @Validated(PassAndEmailGroup.EditPass.class) PassAndEmailDto passAndEmailDto) throws Exception {
        if (adminService.updatePass(passAndEmailDto)) {
            return ResultModel.ok();
        }
        return ResultModel.error();

    }

    @LogByMethod("/admin/manager/updateEmail")
    @ApiOperation(value = "从个人中心页面修改邮箱", notes = "从个人中心页面修改邮箱", response = Response.class)
    @PostMapping("/updateEmail")
    @Transactional(rollbackFor = Exception.class)
    public ResultModel<Object> updateEmail(@RequestBody @Validated(PassAndEmailGroup.EditEmail.class)PassAndEmailDto passAndEmailDto) throws Exception {
        // 对验证码进行校验
        if(!verificationCodeService.validateCode(passAndEmailDto.getEmail(), passAndEmailDto.getCode(), Constants.EmailSource.ADMIN)) {
            return ResultModel.setResult(ResultCodeEnum.CODE_ERROR);
        }
        if (adminService.updateEmail(passAndEmailDto)) {
            return ResultModel.ok();
        }
        return ResultModel.error();

    }

    @LogByMethod("/admin/manager/delAdmin")
    @ApiOperation(value = "删除管理员", notes = "删除管理员", response = Response.class)
    @DeleteMapping("/delAdmin")
    public ResultModel<Object> delAdmin(@RequestBody Set<String> ids) throws Exception {
        for (String id : ids) {
            if (!roleService.validRoleLevelByUserId(id)) {
                return ResultModel.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        List<String> urlList = adminService.delAdmin(ids);
        if (!CollectionUtils.isEmpty(urlList) && qiNiuYunOssProperties.getDeletePicture()) {
            // 删除账号后同时删除其头像
            try {
                for (String url : urlList) {
                    ossService.delete(url);
                }
            } catch (Exception e) {
                log.error("delAdmin success but delete admin avatar failed, adminIds:{}", ids);
            }
        }
        return ResultModel.ok();

    }
}

