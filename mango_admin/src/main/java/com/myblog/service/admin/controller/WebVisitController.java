package com.myblog.service.admin.controller;


import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.entity.dto.WebVisitDto;
import com.myblog.service.security.service.WebVisitService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin/webVisit")
public class WebVisitController {

    @Autowired
    private WebVisitService webVisitService;

    @LogByMethod("/admin/webVisit/getWebVisitByPage")
    @ApiOperation(value = "分页查询用户访问记录", notes = "分页查询用户访问记录", response = Response.class)
    @PostMapping("/getWebVisitByPage")
    public Response getWebVisitByPage(@RequestBody WebVisitDto webVisitDto) throws Exception {
        return webVisitService.getWebVisitByPage(webVisitDto);
    }

    @LogByMethod("/admin/comment/delWebVisit")
    @ApiOperation(value = "删除用户访问记录", notes = "删除用户访问记录", response = Response.class)
    @DeleteMapping("/delWebVisit")
    public Response delWebVisit(@RequestBody Set<String> ids) throws Exception {
        return webVisitService.delWebVisit(ids);
    }
}

