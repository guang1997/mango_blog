package com.myblog.service.base.common;

import java.util.HashMap;
import java.util.Map;

public enum MenuTypeEnum {

    CATALOGUE(0, "目录"),
    MENU(1,"菜单"),
    BUTTON(2, "按钮");

    private Integer code;
    private String name;

    MenuTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static Map<Integer, MenuTypeEnum> codeMap = new HashMap<>();

    static {
        for (MenuTypeEnum menuTypeEnum : MenuTypeEnum.values()) {
            codeMap.put(menuTypeEnum.getCode(), menuTypeEnum);
        }
    }

    public static MenuTypeEnum getMenyTypeEnumByCode(Integer code) {
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
