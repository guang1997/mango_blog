package com.myblog.service.base.common;

import java.util.HashMap;
import java.util.Map;

public enum LinkStatusEnum {

    APPLY(0, "申请中"),
    ONLINE(1,"已上线"),
    REMOVED(2, "已下架")
    ;

    private Integer code;
    private String name;

    LinkStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static Map<Integer, LinkStatusEnum> codeMap = new HashMap<>();

    static {
        for (LinkStatusEnum linkStatusEnum : LinkStatusEnum.values()) {
            codeMap.put(linkStatusEnum.getCode(), linkStatusEnum);
        }
    }

    public static LinkStatusEnum getLinkStatusEnumByCode(Integer code) {
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
