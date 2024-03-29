package com.myblog.service.web.controller;


import java.util.Map;

import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.TagDto;
import com.myblog.service.web.service.TagService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/web/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @LogByMethod(value = "/web/tag/getTagByPage", behavior = BehaviorEnum.TAG)
    @ApiOperation(value = "分页查询标签信息", notes = "分页查询标签信息", response = Response.class)
    @PostMapping("/getTagByPage")
    public ResultModel<Map<String, Object>> getTagByPage(@RequestBody TagDto tagDto) throws Exception {
        return ResultModel.ok(tagService.getTagByPage(tagDto));
    }

}

