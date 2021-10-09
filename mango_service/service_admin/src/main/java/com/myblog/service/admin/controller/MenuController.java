package com.myblog.service.admin.controller;


import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@RestController
@RequestMapping("/admin/menu")
public class MenuController {

    private static Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "获取所有菜单列表", notes = "获取所有菜单列表", response = Response.class)
    @PostMapping("/getAllMenu")
    public Response getAllMenu() {
        Response response = Response.ok();
        try {
            response = menuService.getAllMenu();
        } catch (Exception e) {
            LOGGER.error("getListByDictType Exception:", e);
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
        }
        return response;
    }
}

