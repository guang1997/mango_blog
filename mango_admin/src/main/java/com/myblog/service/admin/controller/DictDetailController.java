package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.DictDetailDto;
import com.myblog.service.admin.service.DictDetailService;
import com.myblog.service.security.annotation.LogByMethod;
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
@CrossOrigin
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
        return dictDetailService.getDictDetailByPage(dictDetailDto);
    }

    @LogByMethod("/admin/dictDetail/getDetailsByDictName")
    @ApiOperation(value = "根据字典类型获取字典数据", notes = "根据字典类型获取字典数据", response = Response.class)
    @PostMapping("/getDetailsByDictName")
    public Response getDetailsByDictName(@RequestBody DictDetailDto dictDetailDto) {
        Response response = Response.ok();
        if (dictDetailDto == null || StringUtils.isBlank(dictDetailDto.getDictName())) {
            LOGGER.error("getDetailsByDictName failed, dictName cannot be null, dictDetailDto:{}", dictDetailDto);
            return response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
        }
        return dictDetailService.getDetailsByDictName(dictDetailDto.getDictName());
    }

    @LogByMethod(value = "/admin/dictDetail/addDictDetail", validate = true)
    @ApiOperation(value = "新增字典详情", notes = "新增字典详情", response = Response.class)
    @PostMapping("/addDictDetail")
    public Response addDictDetail(@RequestBody DictDetailDto dictDetailDto) throws Exception {
        return dictDetailService.addDictDetail(dictDetailDto);
    }

    @LogByMethod(value = "/admin/dictDetail/editDictDetail", validate = true)
    @ApiOperation(value = "修改字典详情", notes = "修改字典详情", response = Response.class)
    @PutMapping("/editDictDetail")
    public Response editDictDetail(@RequestBody DictDetailDto dictDetailDto) throws Exception {
        return dictDetailService.editDictDetail(dictDetailDto);
    }

    @LogByMethod("/admin/dictDetail/delDictDetails")
    @ApiOperation(value = "删除字典详情", notes = "删除字典详情", response = Response.class)
    @DeleteMapping("/delDictDetails")
    public Response delDictDetails(@RequestBody Set<String> ids) throws Exception {
        return dictDetailService.delDictDetails(ids);
    }
}

