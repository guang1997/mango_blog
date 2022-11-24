package com.myblog.service.base.handler.es;

import com.myblog.service.base.annotation.es.EsContextAware;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractEsOperateHandler<T> implements EsContextAware {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Value("${elasticsearch.config.bulk.retryTime}")
    private Integer retryTime;

    @Value("${elasticsearch.config.bulk.timeOut}")
    private Long timeOut;

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

    /**
     * 批量插入
     * @param esModelList
     * @return
     * @throws IOException
     */
    public List<T> bulk(List<T> esModelList) throws IOException {
        BulkRequest request = new BulkRequest();
        request.timeout(TimeValue.timeValueSeconds(timeOut));
        for (T t : esModelList) {
            request.add(buildJson(t));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        if (Objects.isNull(bulkResponse)) {
            getLogger().error("bulkResponse is null, esModelList:{}", esModelList);
            throw new RuntimeException("保存失败");
        }
        BulkItemResponse[] responseItems = bulkResponse.getItems();
        List<T> failedModelList = new ArrayList<>();
        for (BulkItemResponse responseItem : responseItems) {
            if (responseItem.isFailed()) {
                failedModelList.add(esModelList.get(responseItem.getItemId()));
            }
        }
        return failedModelList;
    }

    protected List<T> retry(List<T> esModelList) throws Exception {
        int num = 0;
        while (num++ < retryTime) {
            Thread.sleep(1000);
            esModelList = bulk(esModelList);
            if (CollectionUtils.isEmpty(esModelList)) {
                return null;
            }
        }
        return esModelList;
    }
    protected abstract DocWriteRequest buildJson(T t);

    protected abstract Logger getLogger();
}
