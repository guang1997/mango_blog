package com.myblog.service.admin.controller;

import com.myblog.service.admin.config.FileUploadProperties;
import com.myblog.service.admin.service.OssService;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.FileUtil;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/admin/oss")
public class OssController {

    private static Logger LOGGER = LoggerFactory.getLogger(OssController.class);

    @Autowired
    private OssService ossService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    /**
     * 该方法用于上传文件
     * @return
     */
    @LogByMethod("/admin/oss/uploadAvatar")
    @ApiOperation(value = "个人中心页面上传头像", notes = "个人中心页面上传头像", response = Response.class)
    @PostMapping("/uploadAvatar")
    public Response uploadAvatar(MultipartFile avatar,
                                 @RequestParam("moduleName") String moduleName,
                                 @RequestParam("id") String id) throws Exception {
        // 文件大小验证
        FileUtil.checkSize(fileUploadProperties.getAvatarMaxSize(), avatar.getSize());
        // 验证文件上传的格式
        String fileType = FileUtil.getExtensionName(avatar.getOriginalFilename());
        if(fileType != null && !fileUploadProperties.getAvatarImgType().contains(fileType)){
            throw new RuntimeException("文件格式错误！, 仅支持 " + fileUploadProperties.getAvatarImgType() +" 格式");
        }
        if (StringUtils.isBlank(id)) {
            LOGGER.error("uploadAvatar failed, id cannot be null");
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        Admin admin = adminService.getById(id);
        if (Objects.isNull(admin)) {
            LOGGER.error("uploadAvatar failed, cannot find admin by id:{}", id);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        Response response = ossService.upload(avatar, moduleName);
        // 更新用户数据
        admin.setAvatar(response.getData().get(Constants.ReplyField.URL).toString());
        adminService.updateById(admin);
        return response;
    }

    /**
     * 该方法用于上传文件到七牛云
     * @return
     */
    @LogByMethod("/admin/oss/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件", response = Response.class)
    @PostMapping("/upload")
    public Response upload(MultipartFile file, @RequestParam("moduleName") String moduleName) throws Exception {
        return ossService.upload(file, moduleName);
    }

    /**
     * 该方法用于ckEditor上传图片到七牛云
     * 响应需要特殊的格式
     * 上传成功：{
     *     "uploaded": 1,
     *     "fileName": "foo.jpg",
     *     "url": "/files/foo.jpg"
     *     }
     * 上传失败：{
     *     "uploaded": 0,
     *     "error": {
     *         "message": "The file is too big."
     *     }
     *     }
     * @return
     */
    @LogByMethod("/admin/oss/qiNiuUpload")
    @ApiOperation(value = "ckEditor使用七牛云上传图片", notes = "ckEditor使用七牛云上传图片", response = Response.class)
    @PostMapping("/qiNiuUpload")
    public Object qiNiuUpload(MultipartFile file, @RequestParam("moduleName") String moduleName) throws Exception {
        return ossService.qiNiuUpload(file, moduleName);
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
