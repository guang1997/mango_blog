package com.myblog.service.base.common;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EsBulkBehaviorEnum {

    INDEX("index", "存在就覆盖，不存在就新增"),
    CREATE("create", "存在就报错，不存在就新增"),
    UPDATE("update", "存在就更新，不存在就报错"),
    DELETE("delete", "存在就删除，不存在就报错")
    ;
    private String type;
    private String explain;

    public static boolean isIndex(EsBulkBehaviorEnum type) {
        return Objects.equals(type, INDEX.type);
    }

    public static boolean isCreate(EsBulkBehaviorEnum type) {
        return Objects.equals(type, CREATE.type);
    }

    public static boolean isUpdate(EsBulkBehaviorEnum type) {
        return Objects.equals(type, UPDATE.type);
    }

    public static boolean isDelete(EsBulkBehaviorEnum type) {
        return Objects.equals(type, DELETE.type);
    }
}
