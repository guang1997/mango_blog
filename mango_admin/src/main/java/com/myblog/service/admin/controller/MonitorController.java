package com.myblog.service.admin.controller;

import com.myblog.service.admin.service.MonitorService;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 监控页面 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-28
 */
@CrossOrigin
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @LogByMethod("/admin/monitor/getServers")
    @ApiOperation(value = "查询系统信息", notes = "查询系统信息", response = Response.class)
    @GetMapping("/getServers")
    public ResultModel<Map<String, Object>> getServers() throws Exception {
        return ResultModel.ok(monitorService.getServers());
    }
}
