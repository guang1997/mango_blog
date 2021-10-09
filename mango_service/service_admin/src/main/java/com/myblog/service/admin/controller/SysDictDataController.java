package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.SysDictData;
import com.myblog.service.admin.service.SysDictDataService;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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

    private static Logger LOGGER = LoggerFactory.getLogger(SysDictDataController.class);

    @Autowired
    private SysDictDataService sysDictDataService;

    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = Response.class)
    @PostMapping("/getListByDictType")
    public Response getListByDictType(@RequestParam("dictType") String dictType) {
        Response response = Response.ok();
        if (StringUtils.isEmpty(dictType)) {
            return response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
        }
        try {
            response = sysDictDataService.getListByDictType(dictType);
        } catch (Exception e) {
            LOGGER.error("getListByDictType Exception:", e);
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
        }
        return response;
    }

    @ApiOperation(value = "根据字典类型集合获取字典数据", notes = "根据字典类型集合获取字典数据", response = Response.class)
    @PostMapping("/getListByDictTypeList")
    public Response getListByDictTypeList(@RequestBody List<String> dictTypes) {
        Response response = Response.ok();
        if (CollectionUtils.isEmpty(dictTypes)) {
            return response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
        }
        try {
            response = sysDictDataService.getListByDictTypeList(dictTypes);
        } catch (Exception e) {
            LOGGER.error("getListByDictTypeList Exception:", e);
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
        }
        return response;
    }
}

