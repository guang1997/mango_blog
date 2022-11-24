package com.myblog.service.admin.handler;

import com.myblog.service.admin.entity.dto.BlogDto;
import com.myblog.service.base.annotation.es.EsContext;
import com.myblog.service.base.handler.es.AbstractEsOperateHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EsContext(index = "blog", suffix = "admin", mappingFilePath = "classpath:template/esModel/blog.json")
public class BlogEsOperateHandler extends AbstractEsOperateHandler<BlogDto> {
    @Override
    protected Logger getLogger() {
        return log;
    }
}
