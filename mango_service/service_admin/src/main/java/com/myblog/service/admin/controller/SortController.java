package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.SortDto;
import com.myblog.service.admin.service.SortService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

    @LogByMethod("/admin/sort/addSort")
    @ApiOperation(value = "新增分类", notes = "新增分类", response = Response.class)
    @PostMapping("/addSort")
    public Response addSort(@RequestBody SortDto sortDto) throws Exception {
        if (StringUtils.isBlank(sortDto.getSortName())) {
            LOGGER.error("addSort failed, sortName cannot be null, role:{}", sortDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return sortService.addSort(sortDto);
    }

    @LogByMethod("/admin/sort/editSort")
    @ApiOperation(value = "修改分类", notes = "修改标分类", response = Response.class)
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

