package com.myblog.service.admin.controller;


import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "获取所有菜单列表", notes = "获取所有菜单列表", response = Response.class)
    @PostMapping("/getAllMenu")
    public Response getAllMenu() {
        Response response = Response.ok();
        try {
            response = menuService.getAllMenu();
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @ApiOperation(value = "根据id获取菜单及上级菜单信息", notes = "根据id获取菜单及上级菜单信息", response = Response.class)
    @GetMapping("/getMenuById")
    public Response getMenuById(@RequestParam("id") String id) {
        Response response = Response.ok();
        try {
            response = menuService.getMenuById(id);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @ApiOperation(value = "根据pid获取当前菜单及下级菜单信息", notes = "根据pid获取当前菜单及下级菜单信息", response = Response.class)
    @GetMapping("/getMenusByPid")
    public Response getMenusByPid(@RequestParam(value = "pid", required = false) String pid) {
        Response response = Response.ok();
        try {
            response = menuService.getMenusByPid(pid);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }
}

