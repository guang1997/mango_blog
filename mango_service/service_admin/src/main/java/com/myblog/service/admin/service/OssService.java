package com.myblog.service.admin.service;

import com.myblog.service.base.common.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OssService {
    Response upload(MultipartFile file, String moduleName) throws IOException;

    Response delete(String url);
}
