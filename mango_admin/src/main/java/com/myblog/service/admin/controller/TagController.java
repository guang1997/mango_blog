package com.myblog.service.admin.controller;


import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.admin.service.TagService;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @LogByMethod("/admin/tag/getTagByPage")
    @ApiOperation(value = "分页查询标签", notes = "分页查询标签", response = Response.class)
    @PostMapping("/getTagByPage")
    public ResultModel<Map<String, Object>> getTagByPage(@RequestBody TagDto tagDto) throws Exception {
        return ResultModel.ok(tagService.getTagByPage(tagDto));
    }

    @LogByMethod(value = "/admin/tag/addTag", validate = true)
    @ApiOperation(value = "新增标签", notes = "新增标签", response = Response.class)
    @PostMapping("/addTag")
    public ResultModel<Object> addTag(@RequestBody @Validated TagDto tagDto) throws Exception {
        if (tagService.addTag(tagDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.SAVE_FAILED);
    }

    @LogByMethod(value = "/admin/tag/editTag", validate = true)
    @ApiOperation(value = "修改标签", notes = "修改标签", response = Response.class)
    @PutMapping("/editTag")
    public ResultModel<Object> editTag(@RequestBody @Validated TagDto tagDto) throws Exception {
        if (tagService.editTag(tagDto)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
    }

    @LogByMethod("/admin/tag/delTags")
    @ApiOperation(value = "删除标签", notes = "删除标签", response = Response.class)
    @DeleteMapping("/delTags")
    public ResultModel<Object> delTags(@RequestBody Set<String> ids) throws Exception {
        if (tagService.delTags(ids)) {
            return ResultModel.ok();
        }
        return ResultModel.setResult(ResultCodeEnum.DELETE_FAILED);
    }

}

