package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.service.DictService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/admin/dict")
public class DictController {

    private static Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private DictService dictService;

    @Autowired
    private RedisUtil redisUtil;

    @LogByMethod("/admin/dict/getDictByPage")
    @ApiOperation(value = "分页查询字典", notes = "分页查询字典", response = Response.class)
    @PostMapping("/getDictByPage")
    public Response getDictByPage(@RequestBody DictDto dictDto) throws Exception {
        Response response = Response.ok();
        try {
            response = dictService.getDictByPage(dictDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/dict/addDict")
    @ApiOperation(value = "新增字典", notes = "新增字典", response = Response.class)
    @PostMapping("/addDict")
    public Response addDict(@RequestBody DictDto dictDto) {
        Response response = Response.ok();
        try {
            if (StringUtils.isBlank(dictDto.getDictName())) {
                LOGGER.error("addDict failed, dictName cannot be null, dict:{}", dictDto);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            response = dictService.addDict(dictDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/dict/editDict")
    @ApiOperation(value = "修改字典", notes = "修改字典", response = Response.class)
    @PutMapping("/editDict")
    public Response editDict(@RequestBody DictDto dictDto) {
        Response response = Response.ok();
        try {
            if (StringUtils.isBlank(dictDto.getDictName())) {
                LOGGER.error("editDict failed, dictName cannot be null, dict:{}", dictDto);
                return response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            }
            // 删除缓存中的数据
            redisUtil.delete(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictDto.getDictName());
            response = dictService.editDict(dictDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/dict/delDict")
    @ApiOperation(value = "删除字典", notes = "删除字典", response = Response.class)
    @DeleteMapping("/delDict")
    public Response delTags(@RequestBody Set<String> ids) {
        Response response = Response.ok();
        try {
            response = dictService.delDict(ids);
        } catch (Exception e) {
            response.code(ResultCodeEnum.DELETE_FAILED.getCode()).message(ResultCodeEnum.DELETE_FAILED.getMessage());
            throw e;
        }
        return response;
    }
}

