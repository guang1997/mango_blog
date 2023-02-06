package com.myblog.service.admin.controller;

import com.myblog.service.admin.config.FileUploadProperties;
import com.myblog.service.admin.service.OssService;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.security.annotation.LogByMethod;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.FileUtil;
import com.myblog.service.security.entity.Admin;
import com.myblog.service.security.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/oss")
public class OssController {

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
    public ResultModel<Map<String, Object>> uploadAvatar(MultipartFile avatar,
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
            log.error("uploadAvatar failed, id cannot be null");
            return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        Admin admin = adminService.getById(id);
        if (Objects.isNull(admin)) {
            log.error("uploadAvatar failed, cannot find admin by id:{}", id);
            return ResultModel.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        Map<String, Object> resultMap = ossService.upload(avatar, moduleName);
        // 更新用户数据
        admin.setAvatar(resultMap.get(Constants.ReplyField.URL).toString());
        adminService.updateById(admin);
        return ResultModel.ok(resultMap);
    }

    /**
     * 该方法用于上传文件到七牛云
     * @return
     */
    @LogByMethod("/admin/oss/upload")
    @ApiOperation(value = "上传文件", notes = "上传文件", response = Response.class)
    @PostMapping("/upload")
    public ResultModel<Map<String, Object>> upload(MultipartFile file, @RequestParam("moduleName") String moduleName) throws Exception {
        return ResultModel.ok(ossService.upload(file, moduleName));
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
    public Object qiNiuUpload(@RequestParam("upload") MultipartFile file, @RequestParam("moduleName") String moduleName) throws Exception {
        return ossService.qiNiuUpload(file, moduleName);
    }

    /**
     * 用于从阿里云删除头像
     * @param url
     * @return
     */
//    @LogByMethod("/admin/oss/delete")
//    @ApiOperation(value = "删除文件", notes = "删除文件", response = Response.class)
//    @DeleteMapping("/delete")
//    public Response delete(@RequestBody String url) {
//        if (StringUtils.isBlank(url)) {
//            log.error("oss delete failed, url cannot be null");
//            return Response.setResult(ResultCodeEnum.DELETE_FAILED);
//        }
//        return ossService.delete(url);
//    }
}
