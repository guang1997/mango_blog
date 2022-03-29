package com.myblog.service.admin.entity.dto.monitor;

import lombok.Data;

@Data
public class DiskDto {
    /**
     * 内存总量
     */
    private String total;
    /**
     * 可用的内存
     */
    private String available;
    /**
     * 已使用的内存
     */
    private String used;
    /**
     * 内存使用率
     */
    private String usageRate;
}
