package com.myblog.service.admin.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.MenuTypeEnum;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BaseUtil;
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
import java.util.stream.Collectors;

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

    @LogByMethod("/admin/menu/getSuperior")
    @ApiOperation(value = "根据id获取菜单及上级菜单信息", notes = "根据id获取菜单及上级菜单信息", response = Response.class)
    @GetMapping("/getSuperior")
    public Response getSuperior(@RequestParam("id") String id) {
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

    @LogByMethod("/admin/menu/getChildren")
    @ApiOperation(value = "根据id获取当前菜单id及所有下级菜单的id", notes = "根据id获取当前菜单id及所有下级菜单的id", response = Response.class)
    @GetMapping("/getChildren")
    public Response getChildren(@RequestParam("id") String id) {
        Response response = Response.ok();
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        try {
            MenuDto menuDto = menuService.getMenuById(id);
            menuDtos.add(menuDto);
            List<MenuDto> childrenList = menuService.getMenusByPid(id);
            menuDtos.addAll(menuService.getChildren(childrenList, menuDtos));
            List<String> resultIds = menuDtos.stream()
                    .filter(BaseUtil.distinctByKey(MenuDto::getId))
                    .map(MenuDto::getId)
                    .collect(Collectors.toList());
            response.data(Constants.ReplyField.DATA, resultIds);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/menu/getMenusByPid")
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

    @LogByMethod("/admin/menu/addMenu")
    @ApiOperation(value = "新增菜单", notes = "新增菜单", response = Response.class)
    @PostMapping("/addMenu")
    public Response addMenu(@RequestBody MenuDto menuDto) {
        Response response = Response.ok();
        try {
            MenuTypeEnum menuType = MenuTypeEnum.getMenyTypeEnumByCode(menuDto.getMenuType());
            if (menuType == null) {
                LOGGER.error("addMenu failed, cannot find menuType, menu:{}", menuDto);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            if (Objects.equals(menuType, MenuTypeEnum.CATALOGUE)) {
                menuDto.setComponent("Layout");
            }
            menuDto.setSubCount(0);
            if (!Objects.equals(menuType, MenuTypeEnum.BUTTON)
                    && (StringUtils.isBlank(menuDto.getTitle()) || StringUtils.isBlank(menuDto.getComponent()))) {
                LOGGER.error("addMenu failed, title or component is empty, menu:{}", menuDto);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            response = menuService.addMenu(menuDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/menu/editMenu")
    @ApiOperation(value = "修改菜单", notes = "修改菜单", response = Response.class)
    @PutMapping("/editMenu")
    public Response editMenu(@RequestBody MenuDto menuDto) {
        Response response = Response.ok();
        try {
            MenuTypeEnum menuType = MenuTypeEnum.getMenyTypeEnumByCode(menuDto.getMenuType());
            if (menuType == null) {
                LOGGER.error("editMenu failed, cannot find menuType, menu:{}", menuDto);
                response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
                return response;
            }
            if (Objects.equals(menuType, MenuTypeEnum.CATALOGUE)) {
                menuDto.setComponent("Layout");
            }
            if (!Objects.equals(menuType, MenuTypeEnum.BUTTON)
                    && (StringUtils.isBlank(menuDto.getTitle()) || StringUtils.isBlank(menuDto.getComponent()))) {
                LOGGER.error("editMenu failed, title or component is empty, menu:{}", menuDto);
                response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
                return response;
            }

            response = menuService.editMenu(menuDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/menu/delMenu")
    @ApiOperation(value = "删除菜单", notes = "删除菜单", response = Response.class)
    @DeleteMapping("/delMenu")
    public Response delMenu(@RequestBody Set<String> ids) {
        Response response = Response.ok();

        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        try {
            // 获取所有需要删除的菜单信息
            for (String id : ids) {
                // 获取当前菜单信息
                menuDtos.add(menuService.getMenuById(id));
                // 获取所有子菜单信息
                List<MenuDto> childrenList = menuService.getMenusByPid(id);
                menuDtos.addAll(menuService.getChildren(childrenList, menuDtos));
            }
            // 对id进行去重
            List<String> delIds = menuDtos.stream()
                    .filter(BaseUtil.distinctByKey(MenuDto::getId))
                    .map(MenuDto::getId)
                    .collect(Collectors.toList());
            response = menuService.delMenu(delIds);
        } catch (Exception e) {
            response.code(ResultCodeEnum.DELETE_FAILED.getCode()).message(ResultCodeEnum.DELETE_FAILED.getMessage());
            throw e;
        }
        return response;
    }
}

