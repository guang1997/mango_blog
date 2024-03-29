package com.myblog.service.base.handler.es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.myblog.service.base.common.EsBulkBehaviorEnum;
import com.myblog.service.base.entity.es.BaseEsEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class EsOperateManager {

    @Autowired
    private Map<String, AbstractEsOperateHandler> handlers;

    @Value("${elasticsearch.config.index.create}")
    public boolean createIndex;

    @PostConstruct
    public void init() throws Exception {
        if (!createIndex) {
            return;
        }
        // 创建索引
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }
        for (AbstractEsOperateHandler handler : handlers.values()) {
            if (handler.existIndex(handler.getIndex())) {
                log.info("index:{} already exists, not create it", handler.getIndex());
                continue;
            }
            // 获取文件流
            Resource resource = new ClassPathResource(handler.getMappingFilePath());
            if (!resource.exists()) {
                throw new RuntimeException("cannot find es file by path:" + handler.getMappingFilePath());
            }
            StringBuilder esStrBuilder = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String readLine = "";
                while ((readLine = reader.readLine()) != null) {
                    esStrBuilder.append(readLine);
                }
            }
            String mappingJson = esStrBuilder.toString();
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
            log.info("create elasticsearch index:{} success", handler.getIndex());
        }
    }

    public <T extends BaseEsEntity> boolean createIndex(String index, String mapping, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.createIndex(index, mapping);
    }

    public <T extends BaseEsEntity> boolean deleteIndex(String index, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.deleteIndex(index);
    }

    public <T extends BaseEsEntity> boolean existIndex(String index, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.existIndex(index);
    }

    public <T extends BaseEsEntity> boolean bulk(List<T> esModelList, EsBulkBehaviorEnum type, Class<? extends AbstractEsOperateHandler<T>> clazz)
            throws Exception {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        List<T> failedModelList = handler.bulk(esModelList, type);
        if (CollectionUtils.isEmpty(failedModelList)) {
            return true;
        }
        List<T> retryFailedList = handler.retry(failedModelList, type);
        if (CollectionUtils.isEmpty(retryFailedList)) {
            return true;
        }
        log.error("index:{} bulk failedList:{}", handler.getIndex(), retryFailedList);
        return false;
    }

    public <T extends BaseEsEntity> boolean insert(T esModel, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.insert(esModel);
    }

    public <T extends BaseEsEntity> boolean update(T esModel, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.update(esModel);
    }

    public <T extends BaseEsEntity> boolean delete(String id, Class<? extends AbstractEsOperateHandler<T>> clazz)
        throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.delete(id);
    }

    public <T extends BaseEsEntity> List<T> search(Map<String, Object> param, Class<? extends AbstractEsOperateHandler<T>> clazz,
                                                   Class<T> resultClass) throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.search(param, resultClass);
    }

    public <T extends BaseEsEntity> T searchById(String id, Class<? extends AbstractEsOperateHandler<T>> clazz, Class<T> resultClass) throws IOException {
        AbstractEsOperateHandler<T> handler = getHandler(clazz);
        return handler.searchById(id, resultClass);
    }
    public <T extends BaseEsEntity> AbstractEsOperateHandler<T> getHandler(Class<? extends AbstractEsOperateHandler<T>> clazz) {
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
