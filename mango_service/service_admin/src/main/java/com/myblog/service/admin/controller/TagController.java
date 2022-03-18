package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.service.TagService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.security.entity.dto.RoleDto;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger LOGGER = LoggerFactory.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    @LogByMethod("/admin/tag/getTagByPage")
    @ApiOperation(value = "分页查询标签", notes = "分页查询标签", response = Response.class)
    @PostMapping("/getTagByPage")
    public Response getTagByPage(@RequestBody TagDto tagDto) throws Exception {
        Response response = Response.ok();
        try {
            response = tagService.getTagByPage(tagDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.QUERY_FAILED.getCode()).message(ResultCodeEnum.QUERY_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/tag/addTag")
    @ApiOperation(value = "新增标签", notes = "新增标签", response = Response.class)
    @PostMapping("/addTag")
    public Response addTag(@RequestBody TagDto tagDto) {
        Response response = Response.ok();
        try {
            if (StringUtils.isBlank(tagDto.getTagName())) {
                LOGGER.error("addTag failed, tagName cannot be null, role:{}", tagDto);
                response.code(ResultCodeEnum.SAVE_FAILED.getCode()).message(ResultCodeEnum.SAVE_FAILED.getMessage());
                return response;
            }
            response = tagService.addTag(tagDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/tag/editTag")
    @ApiOperation(value = "修改标签", notes = "修改标签", response = Response.class)
    @PutMapping("/editTag")
    public Response editTag(@RequestBody TagDto tagDto) {
        Response response = Response.ok();
        try {
            response = tagService.editTag(tagDto);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

    @LogByMethod("/admin/tag/delTags")
    @ApiOperation(value = "删除标签", notes = "删除标签", response = Response.class)
    @DeleteMapping("/delTags")
    public Response delTags(@RequestBody Set<String> ids) {
        Response response = Response.ok();
        try {
            response = tagService.delTags(ids);
        } catch (Exception e) {
            response.code(ResultCodeEnum.UPDATE_FAILED.getCode()).message(ResultCodeEnum.UPDATE_FAILED.getMessage());
            throw e;
        }
        return response;
    }

}

