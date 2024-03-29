package com.myblog.service.web.controller;


import java.util.Map;

import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.ResultModel;
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
    public ResultModel<Map<String, Object>> getSortByPage(@RequestBody SortDto sortDto) throws Exception {
        return ResultModel.ok(sortService.getSortByPage(sortDto));
    }
}

