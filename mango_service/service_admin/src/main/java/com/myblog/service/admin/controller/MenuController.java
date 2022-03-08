package com.myblog.service.admin.controller;


import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.MenuTypeEnum;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.service.MenuService;
import com.myblog.service.security.util.TreeUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    private static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

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

    @ApiOperation(value = "新增菜单", notes = "新增菜单", response = Response.class)
    @PostMapping("/addMenu")
    public Response addMenu(@RequestBody Menu menu) {
        Response response = Response.ok();
        try {
            MenuTypeEnum menuType = MenuTypeEnum.getMenyTypeEnumByCode(menu.getMenuType());
            if (menuType == null) {
                LOGGER.error("addMenu failed, cannot find menuType, menu:{}", menu);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            if (Objects.equals(menuType, MenuTypeEnum.CATALOGUE)) {
                menu.setComponent("Layout");
            }
            if (StringUtils.isBlank(menu.getTitle()) || StringUtils.isBlank(menu.getComponent())) {
                LOGGER.error("addMenu failed, title or component is empty, menu:{}", menu);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            response = menuService.addMenu(menu);
        } catch (Exception e) {
            response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
            throw e;
        }
        return response;
    }
}

