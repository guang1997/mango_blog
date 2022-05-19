package com.myblog.service.web.controller;


import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.TagDto;
import com.myblog.service.web.service.TagService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
@CrossOrigin
@RestController
@RequestMapping("/web/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @LogByMethod("/web/tag/getTagByPage")
    @ApiOperation(value = "分页查询标签信息", notes = "分页查询标签信息", response = Response.class)
    @PostMapping("/getTagByPage")
    public Response getTagByPage(@RequestBody TagDto tagDto) throws Exception {
        return tagService.getTagByPage(tagDto);
    }
}

