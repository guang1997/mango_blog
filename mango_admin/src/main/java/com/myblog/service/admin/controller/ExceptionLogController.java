package com.myblog.service.admin.controller;

import java.util.Set;

import com.myblog.service.admin.service.ExceptionLogService;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.security.entity.dto.ExceptionLogDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 异常信息表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-12-16
 */
@CrossOrigin
@RestController
@RequestMapping("/exceptionLog")
public class ExceptionLogController {

    @Autowired
    private ExceptionLogService exceptionLogService;

    @LogByMethod(value = "/admin/exceptionLog/getExceptionByPage", printResponse = false)
    @ApiOperation(value = "分页查询报错信息", notes = "分页查询报错信息", response = Response.class)
    @PostMapping("/getExceptionByPage")
    public Response getExceptionByPage(@RequestBody ExceptionLogDto exceptionLogDto) throws Exception {
        return exceptionLogService.getExceptionByPage(exceptionLogDto);
    }

    @LogByMethod("/admin/exceptionLog/delExceptionLog")
    @ApiOperation(value = "删除报错信息", notes = "删除报错信息", response = Response.class)
    @DeleteMapping("/delExceptionLog")
    public Response delExceptionLog(@RequestBody Set<String> ids) {
        if (exceptionLogService.removeByIds(ids)) {
            return Response.ok();
        }
        return Response.setResult(ResultCodeEnum.DELETE_FAILED);
    }
}

