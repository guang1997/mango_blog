package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.DictDto;
import com.myblog.service.admin.service.DictService;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.RedisConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.RedisUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/dict")
public class DictController {


    @Autowired
    private DictService dictService;

    @Autowired
    private RedisUtil redisUtil;

    @LogByMethod("/admin/dict/getDictByPage")
    @ApiOperation(value = "分页查询字典", notes = "分页查询字典", response = Response.class)
    @PostMapping("/getDictByPage")
    public ResultModel<Map<String, Object>> getDictByPage(@RequestBody DictDto dictDto) throws Exception {
        return ResultModel.ok(dictService.getDictByPage(dictDto));
    }

    @LogByMethod(value = "/admin/dict/addDict", validate = true)
    @ApiOperation(value = "新增字典", notes = "新增字典", response = Response.class)
    @PostMapping("/addDict")
    public ResultModel<Object> addDict(@RequestBody @Validated DictDto dictDto) throws Exception {
        if (dictService.addDict(dictDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
    }

    @LogByMethod(value = "/admin/dict/editDict", validate = true)
    @ApiOperation(value = "修改字典", notes = "修改字典", response = Response.class)
    @PutMapping("/editDict")
    public ResultModel<Object> editDict(@RequestBody @Validated DictDto dictDto) throws Exception {
        if (dictService.editDict(dictDto)) {
            // 删除缓存中的数据
            redisUtil.delete(RedisConstants.REDIS_DICT_TYPE + RedisConstants.DIVISION + dictDto.getDictName());
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
    }

    @LogByMethod("/admin/dict/delDict")
    @ApiOperation(value = "删除字典", notes = "删除字典", response = Response.class)
    @DeleteMapping("/delDict")
    public ResultModel<Object> delTags(@RequestBody Set<String> ids) throws Exception {
        if (dictService.delDict(ids)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.DELETE_FAILED);
    }
}

