package com.myblog.service.base.annotation.es;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsContext {

    /**
     * es的index
     * @return
     */
    String index();

    /**
     * es的type
     * @return
     */
    //String type();

    /**
     * 后缀，web或者admin
     * @return
     */
    String suffix();

    /**
     * es的索引内容文件所在的路径
     * @return
     */
    String mappingFilePath();
}
