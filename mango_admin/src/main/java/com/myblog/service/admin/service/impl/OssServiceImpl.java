package com.myblog.service.admin.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.myblog.service.admin.config.AliyunOssProperties;
import com.myblog.service.admin.config.QiNiuYunOssProperties;
import com.myblog.service.admin.service.OssService;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultCodeEnum;
import com.myblog.service.base.util.JsonUtils;
import com.myblog.service.base.util.ThreadSafeDateFormat;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Objects.*;

@Service
public class OssServiceImpl implements OssService {

    private static Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);

    @Autowired
    private AliyunOssProperties aliyunOssProperties;

    @Autowired
    private Auth auth;

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private QiNiuYunOssProperties qiNiuYunOssProperties;

    @Override
    public Response upload(MultipartFile file, String moduleName) throws Exception {
        Response response = Response.ok();
        String fileName = file.getOriginalFilename(); // 原来的名字
        String date = ThreadSafeDateFormat.format(new Date(), ThreadSafeDateFormat.DATE_PURE_SPLIT);
        String newFileName = UUID.randomUUID().toString() + Constants.Symbol.COMMA2 + fileName;
        // 拼接filename，构建日期路径：avatar/2019/02/26/uuid唯一文件名
        String key = moduleName + Constants.Symbol.COMMA4 + date + Constants.Symbol.COMMA4 + newFileName;
        String token = auth.uploadToken(qiNiuYunOssProperties.getBucket());
        try (InputStream stream = file.getInputStream()) {
            com.qiniu.http.Response res = uploadManager.put(stream, key, token, null, null);
            if (res.isOK() && res.isJson()) {
                DefaultPutRet putRet = JsonUtils.jsonToPojo(res.bodyString(), DefaultPutRet.class);
                if (Objects.isNull(putRet) || StringUtils.isEmpty(putRet.key)) {
                    LOGGER.error("oss upload failed by result:{} is empty, fileName:{}", putRet, fileName);
                    return Response.setResult(ResultCodeEnum.UPLOAD_ERROR);
                }
                String url = "http://" + String.join(Constants.Symbol.COMMA4, qiNiuYunOssProperties.getDomainName(), putRet.key);
                response.data(Constants.ReplyField.URL, url);
                response.data(Constants.ReplyField.UPLOADED, 1);
                response.data(Constants.ReplyField.FILE_NAME, newFileName);
                return response;
            }
        }catch (Exception e) {
            LOGGER.error("oss upload failed, fileName:{}, exception:", fileName, e);
            throw e;
        }

        return Response.error().data(Constants.ReplyField.ERROR, "上传失败，请联系管理员处理");
    }

//    @Override
//    public Response upload(MultipartFile file, String moduleName) throws IOException {
//        String endpoint = aliyunOssProperties.getEndpoint();
//        String accessKeyId = aliyunOssProperties.getKeyId();
//        String accessKeySecret = aliyunOssProperties.getKeySecret();
//        String bucketName = aliyunOssProperties.getBucketName();
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        String fileName = file.getOriginalFilename(); // 原来的名字
//        String date = ThreadSafeDateFormat.format(new Date(), ThreadSafeDateFormat.DATE_PURE_SPLIT);
//        String uuidName = UUID.randomUUID().toString();// uuid
//
//        // 拼接filename，构建日期路径：avatar/2019/02/26/uuid唯一文件名
//        String key = moduleName + Constants.Symbol.COMMA4 + date + Constants.Symbol.COMMA4 + uuidName + Constants.Symbol.COMMA2 + fileName;
//
//        // 上传文件流。
//        String url = "https://" + bucketName + Constants.Symbol.COMMA3 + endpoint + Constants.Symbol.COMMA4 + key;
//        try (InputStream inputStream = file.getInputStream();){
//            ossClient.putObject(bucketName, key, inputStream);
//            // 关闭OSSClient。
//            ossClient.shutdown();
//        } catch (IOException e) {
//            LOGGER.error("oss upload failed, fileName:{}", fileName);
//            throw e;
//        }
//
//        return Response.ok().data(Constants.ReplyField.URL, url);
//    }

    @Override
    public Response delete(String url) {
        try {
            String endpoint = aliyunOssProperties.getEndpoint();
            String accessKeyId = aliyunOssProperties.getKeyId();
            String accessKeySecret = aliyunOssProperties.getKeySecret();
            String bucketName = aliyunOssProperties.getBucketName();

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 拼接前端的路径
            String host = "https://" + bucketName + Constants.Symbol.COMMA3 + endpoint + Constants.Symbol.COMMA4;
            // 通过剪切路径，获取要删除的头像的路径和名称
            String objectName = url.substring(host.length());
            // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
            ossClient.deleteObject(bucketName, objectName);

            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            LOGGER.error("oss delete failed, url:{}", url);
            throw e;
        }
        return Response.ok();
    }

    @Override
    public Object qiNiuUpload(MultipartFile file, String moduleName) {
        Map<String, Object> response = new HashMap<>();
        String fileName = file.getOriginalFilename(); // 原来的名字
        String date = ThreadSafeDateFormat.format(new Date(), ThreadSafeDateFormat.DATE_PURE_SPLIT);
        String newFileName = UUID.randomUUID().toString() + Constants.Symbol.COMMA2 + fileName;
        // 拼接filename，构建日期路径：avatar/2019/02/26/uuid唯一文件名
        String key = moduleName + Constants.Symbol.COMMA4 + date + Constants.Symbol.COMMA4 + newFileName;
        String token = auth.uploadToken(qiNiuYunOssProperties.getBucket());
        try (InputStream stream = file.getInputStream()) {
            com.qiniu.http.Response res = uploadManager.put(stream, key, token, null, null);
            if (res.isOK() && res.isJson()) {
                DefaultPutRet putRet = JsonUtils.jsonToPojo(res.bodyString(), DefaultPutRet.class);
                if (Objects.isNull(putRet) || StringUtils.isEmpty(putRet.key)) {
                    LOGGER.error("oss upload failed by result:{} is empty, fileName:{}", putRet, fileName);
                    return Response.setResult(ResultCodeEnum.UPLOAD_ERROR);
                }
                String url = "http://" + String.join(Constants.Symbol.COMMA4, qiNiuYunOssProperties.getDomainName(), putRet.key);
                response.put(Constants.ReplyField.URL, url);
                response.put(Constants.ReplyField.UPLOADED, 1);
                response.put(Constants.ReplyField.FILE_NAME, newFileName);
                return response;
            }
        }catch (Exception e) {
            LOGGER.error("oss upload failed, fileName:{}, exception:", fileName, e);
        }
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(Constants.ReplyField.MESSAGE, "上传失败，请联系管理员处理");
        response.put(Constants.ReplyField.UPLOADED, 0);
        response.put(Constants.ReplyField.MESSAGE, errorMap);
        return response;
    }
}
