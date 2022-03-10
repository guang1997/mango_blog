package com.myblog.service.security.entity.vo;

import com.myblog.service.base.entity.vo.BaseVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleVo extends BaseVo {

    /**
     * 模糊查询用
     */
    private String blurry;

    private Boolean searchAll;

    private List<Date> createTime;
}
