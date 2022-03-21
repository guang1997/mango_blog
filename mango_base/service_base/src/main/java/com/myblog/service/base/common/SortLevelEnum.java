package com.myblog.service.base.common;

import java.util.HashMap;
import java.util.Map;

public enum SortLevelEnum {
    FIRST_LEVEL(1, "一级分类"),
    SECOND_LEVEL(2,"二级分类"),
    THIRD_LEVEL(3,"三级分类");

    private Integer code;
    private String name;

    SortLevelEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static Map<Integer, SortLevelEnum> codeMap = new HashMap<>();

    static {
        for (SortLevelEnum sortLevelEnum : SortLevelEnum.values()) {
            codeMap.put(sortLevelEnum.getCode(), sortLevelEnum);
        }
    }

    public static SortLevelEnum getSortLevelEnumByCode(Integer code) {
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
