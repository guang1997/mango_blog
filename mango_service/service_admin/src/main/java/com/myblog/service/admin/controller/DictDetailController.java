package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.admin.service.DictDetailService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.RedisConstants;
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
 * 字典详细数据表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/admin/dictDetail")
public class DictDetailController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private DictDetailService dictDetailService;

    @LogByMethod("/admin/dictDetail/getDictDetailByPage")
    @ApiOperation(value = "分页查询字典详情", notes = "分页查询字典详情", response = Response.class)
    @PostMapping("/getDictDetailByPage")
    public Response getDictDetailByPage(@RequestBody DictDetailDto dictDetailDto) throws Exception {
        Response response = Response.ok();
        try {
            response = dictDetailService.getDictDetailByPage(dictDetailDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/dictDetail/addDictDetail")
    @ApiOperation(value = "新增字典详情", notes = "新增字典详情", response = Response.class)
    @PostMapping("/addDictDetail")
    public Response addDictDetail(@RequestBody DictDetailDto dictDetailDto) {
        Response response = Response.ok();
        try {
            if (StringUtils.isBlank(dictDetailDto.getDictLabel())) {
                LOGGER.error("addDictDetail failed, dictLabel cannot be null, dictDetail:{}", dictDetailDto);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            response = dictDetailService.addDictDetail(dictDetailDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/dictDetail/editDictDetail")
    @ApiOperation(value = "修改字典详情", notes = "修改字典详情", response = Response.class)
    @PutMapping("/editDictDetail")
    public Response editDictDetail(@RequestBody DictDetailDto dictDetailDto) {
        Response response = Response.ok();
        try {
            if (StringUtils.isBlank(dictDetailDto.getDictLabel())) {
                LOGGER.error("editDictDetail failed, dictLabel cannot be null, dictDetail:{}", dictDetailDto);
                return response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            }
            response = dictDetailService.editDictDetail(dictDetailDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/dictDetail/delDictDetails")
    @ApiOperation(value = "删除字典详情", notes = "删除字典详情", response = Response.class)
    @DeleteMapping("/delDictDetails")
    public Response delDictDetails(@RequestBody Set<String> ids) {
        Response response = Response.ok();
        try {
            response = dictDetailService.delDictDetails(ids);
        } catch (Exception e) {
            response.code(ResultCodeEnum.DELETE_FAILED.getCode()).message(ResultCodeEnum.DELETE_FAILED.getMessage());
            throw e;
        }
        return response;
    }
}

