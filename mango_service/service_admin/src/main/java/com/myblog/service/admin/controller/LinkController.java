package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.admin.service.LinkService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
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
 * 友情链接表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-24
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/link")
public class LinkController {

    private static Logger LOGGER = LoggerFactory.getLogger(LinkController.class);

    @Autowired
    private LinkService linkService;

    @LogByMethod("/admin/link/getLinkByPage")
    @ApiOperation(value = "分页查询友情链接", notes = "分页查询友情链接", response = Response.class)
    @PostMapping("/getLinkByPage")
    public Response getLinkByPage(@RequestBody LinkDto linkDto) throws Exception {
        return linkService.getLinkByPage(linkDto);
    }

    @LogByMethod(value = "/admin/link/addLink", validate = true)
    @ApiOperation(value = "新增友情链接", notes = "新增友情链接", response = Response.class)
    @PostMapping("/addLink")
    public Response addLink(@RequestBody LinkDto linkDto) throws Exception {
        if (StringUtils.isBlank(linkDto.getUrl()) || StringUtils.isBlank(linkDto.getTitle())) {
            LOGGER.error("addLink failed, url or title cannot be null, link:{}", linkDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return linkService.addLink(linkDto);
    }

    @LogByMethod(value = "/admin/link/editLink", validate = true)
    @ApiOperation(value = "修改友情链接", notes = "修改友情链接", response = Response.class)
    @PutMapping("/editLink")
    public Response editLink(@RequestBody LinkDto linkDto) throws Exception {
        return linkService.editLink(linkDto);
    }

    @LogByMethod("/admin/link/delLink")
    @ApiOperation(value = "删除友情链接", notes = "删除友情链接", response = Response.class)
    @DeleteMapping("/delLink")
    public Response delLink(@RequestBody Set<String> ids) throws Exception {
        return linkService.delLink(ids);
    }
}

