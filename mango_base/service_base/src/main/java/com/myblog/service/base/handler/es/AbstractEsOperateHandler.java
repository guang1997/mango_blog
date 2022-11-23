package com.myblog.service.base.handler.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractEsOperateHandler<T> {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public boolean createIndex(String index, String mapping) throws Exception {
        CreateIndexRequest request = new CreateIndexRequest(index);
        // 索引分片设置
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
        );
        // 设置索引包含的字段
        request.mapping(mapping, XContentType.JSON);
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        if (Objects.isNull(response)) {
            getLogger().error("createIndex:{} failed, response is null", index);
        }
        return response.isAcknowledged();
    }

    public boolean deleteIndex(String index) throws Exception {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        if (Objects.isNull(response)) {
            getLogger().error("deleteIndex:{} failed, response is null", index);
        }
        return response.isAcknowledged();
    }

    protected abstract Logger getLogger();
}
