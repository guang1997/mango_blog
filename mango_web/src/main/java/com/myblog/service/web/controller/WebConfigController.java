package com.myblog.service.web.controller;


import com.myblog.service.base.common.Constants.ReplyField;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.web.service.WebConfigService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * web配置表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2023-01-31
 */
@CrossOrigin
@RestController
@RequestMapping("/web/webConfig")
public class WebConfigController {

    @Autowired
    private WebConfigService webConfigService;

    @LogByMethod("/web/webConfig/getWebConfig")
    @ApiOperation(value = "查询网站配置信息", notes = "查询网站配置信息", response = Response.class)
    @PostMapping("/getWebConfig")
    public Response getWebConfig() throws Exception {
        return Response.ok().data(ReplyField.DATA, webConfigService.getOne(null));
    }
}

