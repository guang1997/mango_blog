package com.myblog.service.base.common;

import java.util.HashMap;
import java.util.Map;

public enum DataScopeEnum {

    ALL(0, "全部"),
    CUSTOM(1,"自定义");

    private Integer code;
    private String name;

    DataScopeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static Map<Integer, DataScopeEnum> codeMap = new HashMap<>();

    static {
        for (DataScopeEnum dataScopeEnum : DataScopeEnum.values()) {
            codeMap.put(dataScopeEnum.getCode(), dataScopeEnum);
        }
    }

    public static DataScopeEnum getDataScopeEnumByCode(Integer code) {
        return codeMap.get(code);
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
