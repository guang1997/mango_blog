package com.myblog.service.base.handler.es;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import com.myblog.service.base.util.FileUtil;
import com.myblog.service.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

@Slf4j
@Component
public class EsOperateManager {

    @Autowired
    private Map<String, AbstractEsOperateHandler> handlers;

    @PostConstruct
    public void init() throws Exception {
        // 创建索引
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }
        for (AbstractEsOperateHandler handler : handlers.values()) {
            if (handler.existIndex(handler.getIndex())) {
                boolean deleteResponse = handler.deleteIndex(handler.getIndex());
                log.info("index:{} already exists, delete it, response:{}", handler.getIndex(), deleteResponse);
            }
            // 获取文件流
            File jsonFile = ResourceUtils.getFile(handler.getMappingFilePath());
            String mappingJson = FileUtil.readString(jsonFile, StandardCharsets.UTF_8);
            if (StringUtils.isBlank(mappingJson)) {
                log.error("EsOperateManager init failed, mappingJson is null, handler:{}, filePath:{}", handler,
                    handler.getMappingFilePath());
                throw new RuntimeException("EsOperateManager初始化失败");
            }
            boolean createResponse = handler.createIndex(handler.getIndex(), mappingJson);
            if (BooleanUtils.isFalse(createResponse)) {
                log.error("EsOperateManager init failed, handler:{}, filePath:{}", handler,
                    handler.getMappingFilePath());
                throw new RuntimeException("EsOperateManager初始化失败");
            }
        }
    }

    public <T> boolean createIndex(String index, String mapping, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.createIndex(index, mapping);
    }

    public <T> boolean deleteIndex(String index, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.deleteIndex(index);
    }

    public <T> boolean existIndex(String index, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.existIndex(index);
    }

    public <T> AbstractEsOperateHandler<T> getHandler(Class<? extends AbstractEsOperateHandler<T>> clazz) {
        if (Objects.isNull(clazz)) {
            log.error("EsOperateManager execute failed, cannot find handler by clazz:{}", clazz);
            throw new RuntimeException("操作失败");
        }
        String simpleName = clazz.getSimpleName();
        char[] nameArr=simpleName.toCharArray();
        nameArr[0]+=32;
        AbstractEsOperateHandler<T> handler = handlers.get(String.valueOf(nameArr));
        if (Objects.isNull(handler)) {
            log.error("EsOperateManager execute failed, cannot find handler by clazz:{}", clazz);
            throw new RuntimeException("操作失败");
        }
        return handler;
    }
}
