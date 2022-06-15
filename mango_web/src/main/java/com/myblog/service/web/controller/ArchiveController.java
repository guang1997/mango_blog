package com.myblog.service.web.controller;


import com.myblog.service.base.common.BehaviorEnum;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.dto.ArchiveDto;
import com.myblog.service.web.service.BlogService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 归档 前端控制器
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-17
 */
@CrossOrigin
@RestController
@RequestMapping("/web/archive")
public class ArchiveController {

    private static Logger LOGGER = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    private BlogService blogService;

    @LogByMethod(value = "/web/archive/getArchives", behavior = BehaviorEnum.ARCHIVE)
    @ApiOperation(value = "查询归档", notes = "查询归档", response = Response.class)
    @PostMapping("/getArchives")
    public Response getArchives(@RequestBody ArchiveDto archiveDto) throws Exception {
        return blogService.getArchives(archiveDto);
    }
}
