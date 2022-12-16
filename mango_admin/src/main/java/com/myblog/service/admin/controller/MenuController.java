package com.myblog.service.admin.controller;


import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.MenuTypeEnum;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.BaseUtil;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.service.MenuService;
import com.myblog.service.security.util.TreeUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @LogByMethod("/admin/menu/getSuperior")
    @ApiOperation(value = "根据id获取同级与上级菜单信息", notes = "根据id获取同级与上级菜单信息", response = Response.class)
    @GetMapping("/getSuperior")
    public Response getSuperior(@RequestParam("id") String id) throws Exception {
        Response response = Response.ok();
        MenuDto menuDto = menuService.getMenuById(id);
        Set<MenuDto> menuDtos = new LinkedHashSet<>(menuService.getSuperior(menuDto, new ArrayList<>()));
        return response.data(Constants.ReplyField.DATA, TreeUtil.toMenuDtoTree(new ArrayList<>(menuDtos), "0"));
    }

    @LogByMethod("/admin/menu/getChildren")
    @ApiOperation(value = "根据id获取当前菜单id及所有下级菜单的id", notes = "根据id获取当前菜单id及所有下级菜单的id", response = Response.class)
    @GetMapping("/getChildren")
    public Response getChildren(@RequestParam("id") String id) throws Exception {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        MenuDto menuDto = menuService.getMenuById(id);
        menuDtos.add(menuDto);
        List<MenuDto> childrenList = menuService.getMenusByPid(id);
        menuDtos.addAll(menuService.getChildren(childrenList, menuDtos));
        // 取到的数据有重复的，过滤
        List<String> resultIds = menuDtos.stream()
                .filter(BaseUtil.distinctByKey(MenuDto::getId))
                .map(MenuDto::getId)
                .collect(Collectors.toList());
        return Response.ok().data(Constants.ReplyField.DATA, resultIds);
    }

    @LogByMethod("/admin/menu/getMenusByPid")
    @ApiOperation(value = "根据pid获取与当前菜单同级别的菜单信息", notes = "根据pid获取与当前菜单同级别的菜单信息", response = Response.class)
    @GetMapping("/getMenusByPid")
    public Response getMenusByPid(@RequestParam(value = "pid", required = false) String pid) throws Exception {
        List<MenuDto> menuDtos = menuService.getMenusByPid(pid);
        return Response.ok().data(Constants.ReplyField.DATA, menuDtos);
    }

    @LogByMethod(value = "/admin/menu/addMenu", validate = true)
    @ApiOperation(value = "新增菜单", notes = "新增菜单", response = Response.class)
    @PostMapping("/addMenu")
    public Response addMenu(@RequestBody MenuDto menuDto) throws Exception {
        MenuTypeEnum menuType = MenuTypeEnum.getMenyTypeEnumByCode(menuDto.getMenuType());
        if (menuType == null) {
            log.error("addMenu failed, cannot find menuType, menu:{}", menuDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        if (Objects.equals(menuType, MenuTypeEnum.CATALOGUE)) {
            menuDto.setComponent("Layout");
        }
        menuDto.setSubCount(0);
        if (!Objects.equals(menuType, MenuTypeEnum.BUTTON)
                && (StringUtils.isBlank(menuDto.getTitle()) || StringUtils.isBlank(menuDto.getComponent()))) {
            log.error("addMenu failed, title or component is empty, menu:{}", menuDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return menuService.addMenu(menuDto);
    }

    @LogByMethod(value = "/admin/menu/editMenu", validate = true)
    @ApiOperation(value = "修改菜单", notes = "修改菜单", response = Response.class)
    @PutMapping("/editMenu")
    public Response editMenu(@RequestBody MenuDto menuDto) throws Exception {
        MenuTypeEnum menuType = MenuTypeEnum.getMenyTypeEnumByCode(menuDto.getMenuType());
        if (menuType == null) {
            log.error("editMenu failed, cannot find menuType, menu:{}", menuDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        if (Objects.equals(menuType, MenuTypeEnum.CATALOGUE)) {
            menuDto.setComponent("Layout");
        }
        if (!Objects.equals(menuType, MenuTypeEnum.BUTTON)
                && (StringUtils.isBlank(menuDto.getTitle()) || StringUtils.isBlank(menuDto.getComponent()))) {
            log.error("editMenu failed, title or component is empty, menu:{}", menuDto);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }

        return menuService.editMenu(menuDto);
    }

    @LogByMethod("/admin/menu/delMenu")
    @ApiOperation(value = "删除菜单", notes = "删除菜单", response = Response.class)
    @DeleteMapping("/delMenu")
    public Response delMenu(@RequestBody Set<String> ids) throws Exception {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
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
        return menuService.delMenu(delIds);
    }
}

