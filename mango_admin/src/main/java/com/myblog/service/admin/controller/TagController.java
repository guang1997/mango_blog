package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.service.TagService;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @LogByMethod("/admin/tag/getTagByPage")
    @ApiOperation(value = "分页查询标签", notes = "分页查询标签", response = Response.class)
    @PostMapping("/getTagByPage")
    public Response getTagByPage(@RequestBody TagDto tagDto) throws Exception {
        return tagService.getTagByPage(tagDto);
    }

    @LogByMethod(value = "/admin/tag/addTag", validate = true)
    @ApiOperation(value = "新增标签", notes = "新增标签", response = Response.class)
    @PostMapping("/addTag")
    public Response addTag(@RequestBody TagDto tagDto) throws Exception {
        return tagService.addTag(tagDto);
    }

    @LogByMethod(value = "/admin/tag/editTag", validate = true)
    @ApiOperation(value = "修改标签", notes = "修改标签", response = Response.class)
    @PutMapping("/editTag")
    public Response editTag(@RequestBody TagDto tagDto) throws Exception {
        return tagService.editTag(tagDto);
    }

    @LogByMethod("/admin/tag/delTags")
    @ApiOperation(value = "删除标签", notes = "删除标签", response = Response.class)
    @DeleteMapping("/delTags")
    public Response delTags(@RequestBody Set<String> ids) throws Exception {
        return tagService.delTags(ids);
    }

}

