package com.myblog.service.security.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myblog.service.base.entity.vo.BaseVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuVo extends BaseVo {

    private String id;

    private String pid;

    private Boolean hidden;

//    private String redirect;

//    private Boolean alwaysShow;

    private String name;

    private String component;

    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date createTime;

    private Meta meta;

    private List<MenuVo> children;
}
