package com.myblog.service.admin.controller;

import com.myblog.service.admin.service.MonitorService;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @LogByMethod("/admin/monitor/getServers")
    @ApiOperation(value = "查询系统信息", notes = "查询系统信息", response = Response.class)
    @GetMapping("/getServers")
    public Response getServers() throws Exception {
        return monitorService.getServers();
    }
}
