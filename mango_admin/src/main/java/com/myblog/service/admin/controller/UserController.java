package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.UserDto;
import com.myblog.service.admin.service.UserService;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.security.entity.dto.AdminDto;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin/user")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @LogByMethod("/admin/user/getUserByPage")
    @ApiOperation(value = "分页查询网站用户信息", notes = "分页查询网站用户信息", response = Response.class)
    @PostMapping("/getUserByPage")
    public Response getUserByPage(@RequestBody UserDto userDto) throws Exception {
        return userService.getUserByPage(userDto);
    }

    @LogByMethod("/admin/user/editUser")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", response = Response.class)
    @PutMapping("/editUser")
    public Response editUser(@RequestBody UserDto userDto) throws Exception {
        return userService.editUser(userDto);
    }
}

