package com.myblog.service.base.handler.es;

import com.myblog.service.base.annotation.es.EsContextAware;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Objects;

public abstract class AbstractEsOperateHandler<T> implements EsContextAware {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     * @param index
     * @param mapping
     * @return
     * @throws IOException
     */
    public boolean createIndex(String index, String mapping) throws IOException {
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

    /**
     * 删除索引
     * @param index
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        if (Objects.isNull(response)) {
            getLogger().error("deleteIndex:{} failed, response is null", index);
        }
        return response.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public boolean existIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    protected abstract Logger getLogger();
}
