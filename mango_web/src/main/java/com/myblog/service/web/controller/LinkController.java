package com.myblog.service.web.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.LinkDto;
import com.myblog.service.web.service.LinkService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-09
 */
@CrossOrigin
@RestController
@RequestMapping("/web/link")
public class LinkController {

    private static Logger LOGGER = LoggerFactory.getLogger(LinkController.class);

    @Autowired
    private LinkService linkService;

    @LogByMethod("/web/link/getFriendLink")
    @ApiOperation(value = "查询友链信息", notes = "查询友链信息", response = Response.class)
    @PostMapping("/getFriendLink")
    public Response getFriendLink(@RequestBody LinkDto linkDto) throws Exception {
        return linkService.getFriendLink(linkDto);
    }
}

