package com.myblog.service.admin.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class Meta {

    private List<String> roles;

    private String title;

    private String icon;

    private boolean noCache;

    private boolean breadcrumb;

    private boolean affix;
}
