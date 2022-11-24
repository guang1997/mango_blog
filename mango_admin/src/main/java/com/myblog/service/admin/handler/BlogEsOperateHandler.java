package com.myblog.service.admin.handler;

import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.admin.entity.dto.es.BlogEsDto;
import com.myblog.service.base.annotation.es.EsContext;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.handler.es.AbstractEsOperateHandler;
import com.myblog.service.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EsContext(index = "blog", suffix = "admin", mappingFilePath = "classpath:template/esModel/blog.json")
public class BlogEsOperateHandler extends AbstractEsOperateHandler<BlogEsDto> {

    @Override
    protected DocWriteRequest buildJson(BlogEsDto blogEsDto) {
        IndexRequest request = new IndexRequest(getIndex());
        request.id(blogEsDto.getId());
        request.source(XContentType.JSON, JsonUtils.objectToJson(blogEsDto));
        return request;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
