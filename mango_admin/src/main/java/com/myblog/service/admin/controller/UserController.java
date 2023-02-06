package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.UserDto;
import com.myblog.service.admin.service.UserService;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-18
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @LogByMethod("/admin/user/getUserByPage")
    @ApiOperation(value = "分页查询网站用户信息", notes = "分页查询网站用户信息", response = Response.class)
    @PostMapping("/getUserByPage")
    public ResultModel<Map<String, Object>> getUserByPage(@RequestBody UserDto userDto) throws Exception {
        return ResultModel.ok(userService.getUserByPage(userDto));
    }

    @LogByMethod("/admin/user/editUser")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", response = Response.class)
    @PutMapping("/editUser")
    public ResultModel<Object> editUser(@RequestBody UserDto userDto) throws Exception {
        if (userService.editUser(userDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
    }
}

