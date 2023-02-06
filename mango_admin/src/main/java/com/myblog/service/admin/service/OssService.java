package com.myblog.service.admin.service;

import com.myblog.service.base.common.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OssService {
    Map<String, Object> upload(MultipartFile file, String moduleName) throws Exception;

    Response delete(String url);

    Object qiNiuUpload(MultipartFile file, String moduleName);
}
