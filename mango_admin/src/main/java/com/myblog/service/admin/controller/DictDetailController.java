package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.admin.service.DictDetailService;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
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
 * 字典详细数据表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/dictDetail")
public class DictDetailController {

    @Autowired
    private DictDetailService dictDetailService;

    @LogByMethod("/admin/dictDetail/getDictDetailByPage")
    @ApiOperation(value = "分页查询字典详情", notes = "分页查询字典详情", response = Response.class)
    @PostMapping("/getDictDetailByPage")
    public ResultModel<Map<String, Object>> getDictDetailByPage(@RequestBody DictDetailDto dictDetailDto) throws Exception {
        return ResultModel.ok(dictDetailService.getDictDetailByPage(dictDetailDto));
    }

    @LogByMethod("/admin/dictDetail/getDetailsByDictName")
    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = Response.class)
    @PostMapping("/getDetailsByDictName")
    public ResultModel<Map<String, Object>> getDetailsByDictName(@RequestBody DictDetailDto dictDetailDto) {
        return ResultModel.ok(dictDetailService.getDetailsByDictName(dictDetailDto.getDictName()));
    }

    @LogByMethod(value = "/admin/dictDetail/addDictDetail", validate = true)
    @ApiOperation(value = "新增字典详情", notes = "新增字典详情", response = Response.class)
    @PostMapping("/addDictDetail")
    public ResultModel<Object> addDictDetail(@RequestBody @Validated(value = {DictDetailDto.DictDetailValidGroup.Add.class, DictDetailDto.DictDetailValidGroup.Update.class}) DictDetailDto dictDetailDto) throws Exception {
        if (dictDetailService.addDictDetail(dictDetailDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
    }

    @LogByMethod(value = "/admin/dictDetail/editDictDetail", validate = true)
    @ApiOperation(value = "修改字典详情", notes = "修改字典详情", response = Response.class)
    @PutMapping("/editDictDetail")
    public ResultModel<Object> editDictDetail(@RequestBody @Validated(value = {DictDetailDto.DictDetailValidGroup.Add.class, DictDetailDto.DictDetailValidGroup.Update.class}) DictDetailDto dictDetailDto) throws Exception {
        if (dictDetailService.editDictDetail(dictDetailDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
    }

    @LogByMethod("/admin/dictDetail/delDictDetails")
    @ApiOperation(value = "删除字典详情", notes = "删除字典详情", response = Response.class)
    @DeleteMapping("/delDictDetails")
    public ResultModel<Object> delDictDetails(@RequestBody Set<String> ids) throws Exception {
        if (dictDetailService.delDictDetails(ids)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.DELETE_FAILED);
    }
}

