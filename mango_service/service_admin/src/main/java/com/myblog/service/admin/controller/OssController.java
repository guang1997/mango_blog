package com.myblog.service.admin.controller;

import com.myblog.service.admin.service.OssService;
import com.myblog.service.base.annotation.aspect.LogByMethod;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/admin/oss")
public class OssController {

    private static Logger LOGGER = LoggerFactory.getLogger(OssController.class);

    @Autowired
    private OssService ossService;

    /**
     * 该方法用于上传文件
     * @return
     */
    @LogByMethod("/admin/oss/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件", response = Response.class)
    @PostMapping("/upload")
    public Response upload(MultipartFile file, @RequestParam("moduleName") String moduleName) throws IOException {
        return ossService.upload(file, moduleName);
    }

    /**
     * 用于从阿里云删除头像
     * @param url
     * @return
     */
    @LogByMethod("/admin/oss/delete")
    @ApiOperation(value = "删除文件", notes = "删除文件", response = Response.class)
    @DeleteMapping("/delete")
    public Response delete(@RequestBody String url) {
        if (StringUtils.isBlank(url)) {
            LOGGER.error("oss delete failed, url cannot be null");
            return Response.setResult(ResultCodeEnum.DELETE_FAILED);
        }
        return ossService.delete(url);
    }
}
