package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.SortDto;
import com.myblog.service.admin.service.SortService;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 博客分类表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/sort")
public class SortController {

    private static Logger LOGGER = LoggerFactory.getLogger(SortController.class);

    @Autowired
    private SortService sortService;

    @LogByMethod("/admin/sort/getSortByPage")
    @ApiOperation(value = "分页查询博客分类", notes = "分页查询博客分类", response = Response.class)
    @PostMapping("/getSortByPage")
    public Response getSortByPage(@RequestBody SortDto sortDto) throws Exception {
        return sortService.getSortByPage(sortDto);
    }

    @LogByMethod(value = "/admin/sort/addSort", validate = true)
    @ApiOperation(value = "新增分类", notes = "新增分类", response = Response.class)
    @PostMapping("/addSort")
    public Response addSort(@RequestBody SortDto sortDto) throws Exception {
        return sortService.addSort(sortDto);
    }

    @LogByMethod(value = "/admin/sort/editSort", validate = true)
    @ApiOperation(value = "修改分类", notes = "修改分类", response = Response.class)
    @PutMapping("/editSort")
    public Response editSort(@RequestBody SortDto sortDto) throws Exception {
        return sortService.editSort(sortDto);
    }

    @LogByMethod("/admin/sort/delSorts")
    @ApiOperation(value = "删除分类", notes = "删除分类", response = Response.class)
    @DeleteMapping("/delSorts")
    public Response delSorts(@RequestBody Set<String> ids) throws Exception {
        return sortService.delSorts(ids);
    }

}

