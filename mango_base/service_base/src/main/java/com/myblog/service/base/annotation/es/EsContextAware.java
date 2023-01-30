package com.myblog.service.base.annotation.es;

import java.util.Objects;

public interface EsContextAware {

    default String getIndex() {
        EsContext exContext = getClass().getAnnotation(EsContext.class);
        if (Objects.nonNull(exContext)) {
            return exContext.index();
        }
        return "";
    }

    default String getType() {
        EsContext exContext = getClass().getAnnotation(EsContext.class);
        if (Objects.nonNull(exContext)) {
            return exContext.type();
        }
        return "";
    }

    default String getMappingFilePath() {
        EsContext exContext = getClass().getAnnotation(EsContext.class);
        if (Objects.nonNull(exContext)) {
            return exContext.mappingFilePath();
        }
        return "";
    }
}
