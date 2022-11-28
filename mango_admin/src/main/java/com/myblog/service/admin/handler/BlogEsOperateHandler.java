package com.myblog.service.admin.handler;

import com.myblog.service.admin.entity.dto.es.BlogEsDto;
import com.myblog.service.base.annotation.es.EsContext;
import com.myblog.service.base.common.EsBulkBehaviorEnum;
import com.myblog.service.base.handler.es.AbstractEsOperateHandler;
import com.myblog.service.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EsContext(index = "blog", suffix = "admin", mappingFilePath = "classpath:template/esModel/blog.json")
public class BlogEsOperateHandler extends AbstractEsOperateHandler<BlogEsDto> {

    @Override
    protected DocWriteRequest buildBulkJson(BlogEsDto blogEsDto, EsBulkBehaviorEnum type) {
        switch (type) {
            case INDEX:
            case CREATE:
                return buildIndexRequest(blogEsDto);
            case UPDATE:
                return buildUpdateRequest(blogEsDto);
            case DELETE:
                return buildDeleteRequest(blogEsDto);
            default:
                throw new RuntimeException("不支持的类型:" + type.getType());
        }
    }

    private DeleteRequest buildDeleteRequest(BlogEsDto blogEsDto) {
        return new DeleteRequest().id(blogEsDto.getId());
    }

    private IndexRequest buildIndexRequest(BlogEsDto blogEsDto) {
        return new IndexRequest(getIndex())
            .id(blogEsDto.getId())
            .source(buildInsertJson(blogEsDto));
    }

    private UpdateRequest buildUpdateRequest(BlogEsDto blogEsDto) {
        return new UpdateRequest()
            .index(getIndex())
            .id(blogEsDto.getId())
            .doc(buildUpdateJson(blogEsDto));
    }

    @Override
    protected String buildInsertJson(BlogEsDto esModel) {
        return JsonUtils.objectToJson(esModel);
    }

    @Override
    protected String buildUpdateJson(BlogEsDto esModel) {
        return JsonUtils.objectToJson(esModel);
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
