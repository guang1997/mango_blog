package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.WebConfig;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.service.WebConfigService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Constants.ReplyField;
import com.myblog.service.base.common.Response;
import com.myblog.service.security.annotation.LogByMethod;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * web配置表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2023-01-30
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/webConfig")
public class WebConfigController {

    @Autowired
    private WebConfigService webConfigService;

    @LogByMethod("/admin/webConfig/getWebConfig")
    @ApiOperation(value = "查询网站配置信息", notes = "查询网站配置信息", response = Response.class)
    @PostMapping("/getWebConfig")
    public Response getWebConfig() throws Exception {
        return Response.ok().data(ReplyField.DATA, webConfigService.getOne(null));
    }

    @LogByMethod("/admin/webConfig/editWebConfig")
    @ApiOperation(value = "修改网站配置信息", notes = "修改网站配置信息", response = Response.class)
    @PostMapping("/editWebConfig")
    public Response editWebConfig(@RequestBody WebConfig webConfig) throws Exception {
        return Response.ok().data(ReplyField.DATA, webConfigService.saveOrUpdate(webConfig));
    }
}

