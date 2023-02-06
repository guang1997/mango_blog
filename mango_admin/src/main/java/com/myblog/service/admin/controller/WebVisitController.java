package com.myblog.service.admin.controller;


import com.myblog.service.admin.service.WebVisitService;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.entity.dto.WebVisitDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Web访问记录表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-13
 */
@CrossOrigin
@RestController
@RequestMapping("/webVisit")
public class WebVisitController {

    @Autowired
    private WebVisitService webVisitService;

    @LogByMethod("/admin/webVisit/getWebVisitByPage")
    @ApiOperation(value = "分页查询用户访问记录", notes = "分页查询用户访问记录", response = Response.class)
    @PostMapping("/getWebVisitByPage")
    public ResultModel<Map<String, Object>> getWebVisitByPage(@RequestBody WebVisitDto webVisitDto) throws Exception {
        return ResultModel.ok(webVisitService.getWebVisitByPage(webVisitDto));
    }

    @LogByMethod("/admin/comment/delWebVisit")
    @ApiOperation(value = "删除用户访问记录", notes = "删除用户访问记录", response = Response.class)
    @DeleteMapping("/delWebVisit")
    public ResultModel<Object> delWebVisit(@RequestBody Set<String> ids) throws Exception {
        if (webVisitService.delWebVisit(ids)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.DELETE_FAILED);
    }
}

