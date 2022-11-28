package com.myblog.service.base.handler.es;

import com.myblog.service.base.annotation.es.EsContextAware;
import com.myblog.service.base.common.EsBulkBehaviorEnum;
import com.myblog.service.base.entity.es.BaseEsEntity;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractEsOperateHandler<T extends BaseEsEntity> implements EsContextAware {
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
     * 批量操作，暂时只支持单种批量方式，如批量插入或者更新或者删除
     * @param esModelList
     * @return
     * @throws IOException
     */
    public List<T> bulk(List<T> esModelList, EsBulkBehaviorEnum type) throws IOException {
        BulkRequest request = new BulkRequest();
        request.timeout(TimeValue.timeValueSeconds(timeOut));
        for (T t : esModelList) {
            request.add(buildBulkJson(t, type));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        if (Objects.isNull(bulkResponse)) {
            getLogger().error("bulkResponse is null, esModelList:{}", esModelList);
            throw new RuntimeException("保存失败");
        }
        BulkItemResponse[] responseItems = bulkResponse.getItems();
        List<T> failedModelList = new ArrayList<>();
        for (BulkItemResponse responseItem : responseItems) {
            int itemId = responseItem.getItemId();
            if (responseItem.isFailed()) {
                failedModelList.add(esModelList.get(responseItem.getItemId()));
            }
        }
        return failedModelList;
    }

    /**
     * 向es中插入单条数据
     * @param esModel
     * @return
     * @throws IOException
     */
    public boolean insert(T esModel) throws IOException {
        IndexRequest request = new IndexRequest(getIndex());
        request.id(esModel.getId());
        request.source(buildInsertJson(esModel), XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        if (Objects.isNull(response)) {
            getLogger().error("insert failed, indexResponse is null, esModel:{}", esModel);
            throw new RuntimeException("保存失败");
        }
        if (!Objects.equals(response.status(), RestStatus.OK) || !Objects.equals(response.status(), RestStatus.CREATED)) {
            getLogger().error("insert es failed, errorCode:{}, esModel:{}", response.status().getStatus(), esModel);
            return false;
        }
        return true;
    }

    /**
     * 更新es中的单条数据
     * @param esModel
     * @return
     * @throws IOException
     */
    public boolean update(T esModel) throws IOException {
        UpdateRequest request = new UpdateRequest();
        request.index(getIndex())
               .id(esModel.getId())
               .doc(buildUpdateJson(esModel), XContentType.JSON)
               // 有就更新，没有就新增
               .docAsUpsert(true);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        if (Objects.isNull(response)) {
            getLogger().error("update failed, indexResponse is null, esModel:{}", esModel);
            throw new RuntimeException("保存失败");
        }
        if (!Objects.equals(response.status(), RestStatus.OK) || !Objects.equals(response.status(), RestStatus.CREATED)) {
            getLogger().error("update es failed, errorCode:{}, esModel:{}", response.status().getStatus(), esModel);
            return false;
        }
        return true;
    }

    /**
     * 删除es中的单条数据
     * @param id
     * @return
     * @throws IOException
     */
    public boolean delete(String id) throws IOException {
        DeleteRequest request = new DeleteRequest(getIndex());
        request.id(id);
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        if (Objects.isNull(response)) {
            getLogger().error("delete failed, indexResponse is null, id:{}", id);
            throw new RuntimeException("保存失败");
        }
        if (!Objects.equals(response.status(), RestStatus.OK)) {
            getLogger().error("delete es failed, errorCode:{}, id:{}", response.status().getStatus(), id);
            return false;
        }
        return true;
    }

    protected List<T> retry(List<T> esModelList, EsBulkBehaviorEnum type) throws Exception {
        int num = 0;
        while (num++ < retryTime) {
            Thread.sleep(1000);
            esModelList = bulk(esModelList, type);
            if (CollectionUtils.isEmpty(esModelList)) {
                return null;
            }
        }
        return esModelList;
    }
    protected abstract DocWriteRequest buildBulkJson(T t, EsBulkBehaviorEnum type);

    protected abstract String buildInsertJson(T esModel);

    protected abstract String buildUpdateJson(T esModel);
    protected abstract Logger getLogger();
}
