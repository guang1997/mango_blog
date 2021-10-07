package com.myblog.service.admin.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuVo {

    private String id;

    private String pid;

    private boolean hidden;

    private String redirect;

    private boolean alwaysShow;

    private String name;

    private String component;

    private String path;

    private Meta meta;

    private List<MenuVo> children;
}
