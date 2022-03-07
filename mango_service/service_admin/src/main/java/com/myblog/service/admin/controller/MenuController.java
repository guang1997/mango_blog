package com.myblog.service.admin.controller;


import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.service.MenuService;
import com.myblog.service.security.util.TreeUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @ApiOperation(value = "根据id获取菜单及上级菜单信息", notes = "根据id获取菜单及上级菜单信息", response = Response.class)
    @GetMapping("/getMenuById")
    public Response getMenuById(@RequestParam("id") String id) {
        Response response = Response.ok();
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        try {
            MenuDto menuDto = menuService.getMenuById(id);
            menuDtos.addAll(menuService.getSuperior(menuDto, new ArrayList<>()));
            response.data(Constants.ReplyField.DATA, TreeUtil.toMenuDtoTree(new ArrayList<>(menuDtos), "0"));
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @ApiOperation(value = "根据pid获取与当前菜单同级别的菜单信息", notes = "根据pid获取与当前菜单同级别的菜单信息", response = Response.class)
    @GetMapping("/getMenusByPid")
    public Response getMenusByPid(@RequestParam(value = "pid", required = false) String pid) {
        Response response = Response.ok();
        try {
            List<MenuDto> menuDtos = menuService.getMenusByPid(pid);
            response.data(Constants.ReplyField.DATA, menuDtos);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }
}

