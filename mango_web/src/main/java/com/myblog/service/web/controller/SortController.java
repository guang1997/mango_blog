package com.myblog.service.web.controller;


import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.SortDto;
import com.myblog.service.web.service.SortService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 博客分类表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/web/sort")
public class SortController {

    @Autowired
    private SortService sortService;

    @LogByMethod(value = "/web/sort/getSortByPage", behavior = BehaviorEnum.SORT)
    @ApiOperation(value = "分页查询分类信息", notes = "分页查询分类信息", response = Response.class)
    @PostMapping("/getSortByPage")
    public Response getSortByPage(@RequestBody SortDto sortDto) throws Exception {
        return sortService.getSortByPage(sortDto);
    }

    @LogByMethod(value = "/web/sort/initSortByPage")
    @ApiOperation(value = "首页查询分类信息", notes = "首页查询分类信息", response = Response.class)
    @PostMapping("/initSortByPage")
    public Response initSortByPage(@RequestBody SortDto sortDto) throws Exception {
        return sortService.getSortByPage(sortDto);
    }
}

