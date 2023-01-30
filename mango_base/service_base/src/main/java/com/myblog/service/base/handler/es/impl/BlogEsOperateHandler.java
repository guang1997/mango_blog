package com.myblog.service.base.handler.es.impl;

import com.myblog.service.base.annotation.es.EsContext;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.Constants.EsContants;
import com.myblog.service.base.common.EsBulkBehaviorEnum;
import com.myblog.service.base.handler.es.AbstractEsOperateHandler;
import com.myblog.service.base.handler.es.entity.BlogEsDto;
import com.myblog.service.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@EsContext(index = "blog", mappingFilePath = "template/esModel/blog.json")
public class BlogEsOperateHandler extends AbstractEsOperateHandler<BlogEsDto> {

    private static final String HIGHLIGHT_FIELD = "content";
    private static final String PRE_TAG = "<span style='color:red;'>";
    private static final String POST_TAG = "</span>";

    @Override
    protected BlogEsDto buildResultModel(SearchHit hit, Class<BlogEsDto> resultClass) {
        BlogEsDto blogEsDto = JsonUtils.jsonToPojo(hit.getSourceAsString(), resultClass);
        if (Objects.isNull(blogEsDto)) {
            log.warn("jsonToPojo failed, es source:{}", hit.getSourceAsString());
            return new BlogEsDto();
        }
        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        if (!CollectionUtils.isEmpty(highlightFields)) {
            HighlightField highlightField = highlightFields.get(HIGHLIGHT_FIELD);
            if (Objects.isNull(highlightField)) {
                return blogEsDto;
            }
            Text[] fragments = highlightField.getFragments();
            if (Objects.isNull(fragments) || Objects.equals(0, fragments.length)) {
                return blogEsDto;
            }
            blogEsDto.setContent(fragments[0].string());
        }
        return blogEsDto;
    }

    @Override
    protected SearchSourceBuilder buildSearchJson(Map<String, Object> param) {
        Object blogContent = param.get(Constants.EsContants.BLOG_CONTENT);
        if (Objects.isNull(blogContent) || StringUtils.isBlank(blogContent.toString())) {
            log.error("buildSearchJson failed, param is illegal, paramMap:{}", param);
            throw new RuntimeException("查询失败");
        }
        // 高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(HIGHLIGHT_FIELD)
                        //手动前缀标签
                        .preTags(PRE_TAG)
                        .postTags(POST_TAG);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders
                .fuzzyQuery(Constants.EsContants.BLOG_CONTENT, String.valueOf(blogContent))
        ).highlighter(highlightBuilder);
        return searchSourceBuilder;
    }

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
        return new DeleteRequest(getIndex(), getType(), blogEsDto.getId());
    }

    private IndexRequest buildIndexRequest(BlogEsDto blogEsDto) {
        return new IndexRequest(getIndex(), getType())
                .id(blogEsDto.getId())
                .source(buildInsertJson(blogEsDto), XContentType.JSON);
    }

    private UpdateRequest buildUpdateRequest(BlogEsDto blogEsDto) {
        return new UpdateRequest()
                .index(getIndex())
                .type(getType())
                .id(blogEsDto.getId())
                .doc(buildUpdateJson(blogEsDto), XContentType.JSON);
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
