package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.Sort;
import com.myblog.service.admin.entity.dto.SortDto;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.service.SortService;
import com.myblog.service.admin.service.TagService;
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
        Response response = Response.ok();
        try {
            response = sortService.getSortByPage(sortDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/sort/addSort")
    @ApiOperation(value = "新增分类", notes = "新增分类", response = Response.class)
    @PostMapping("/addSort")
    public Response addSort(@RequestBody SortDto sortDto) {
        Response response = Response.ok();
        try {
            if (StringUtils.isBlank(sortDto.getSortName())) {
                LOGGER.error("addSort failed, sortName cannot be null, role:{}", sortDto);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            response = sortService.addSort(sortDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/sort/editSort")
    @ApiOperation(value = "修改分类", notes = "修改标分类", response = Response.class)
    @PutMapping("/editSort")
    public Response editSort(@RequestBody SortDto sortDto) {
        Response response = Response.ok();
        try {
            response = sortService.editSort(sortDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/sort/delSorts")
    @ApiOperation(value = "删除分类", notes = "删除分类", response = Response.class)
    @DeleteMapping("/delSorts")
    public Response delSorts(@RequestBody Set<String> ids) {
        Response response = Response.ok();
        try {
            response = sortService.delSorts(ids);
        } catch (Exception e) {
            response.code(ResultCodeEnum.DELETE_FAILED.getCode()).message(ResultCodeEnum.DELETE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

}

