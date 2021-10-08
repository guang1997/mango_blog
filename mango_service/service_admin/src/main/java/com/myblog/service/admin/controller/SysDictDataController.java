package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.SysDictData;
import com.myblog.service.admin.service.SysDictDataService;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-08
 */
@RestController
@RequestMapping("/admin/sysDictData")
public class SysDictDataController {

    @Autowired
    private SysDictDataService sysDictDataService;

    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = Response.class)
    @PostMapping("/getListByDictType")
    public Response getListByDictType(@RequestParam("dictType") String dictType) {
        Response response = Response.ok();
        if (StringUtils.isEmpty(dictType)) {
            return response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
        }
        response = sysDictDataService.getListByDictType(dictType);
        return response;
    }
}

