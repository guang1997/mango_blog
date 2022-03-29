package com.myblog.service.admin.entity.dto.monitor;

import lombok.Data;

@Data
public class SysDto {

    /**
     * 系统总的信息
     */
    private String systemInfo;

    /**
     * 运行天数
     */
    private String day;

    /**
     * ip
     */
    private String ip;
}
