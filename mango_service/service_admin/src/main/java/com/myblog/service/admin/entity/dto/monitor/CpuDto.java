package com.myblog.service.admin.entity.dto.monitor;

import lombok.Data;

@Data
public class CpuDto {
    /**
     * 名字
     */
    private String name;

    /**
     * 物理cpu
     */
    private String cpuNum;

    /**
     * cpu核心 + 个物理核心
     */
    private String core;

    /**
     * cpu核心
     */
    private Integer coreNumber;

    /**
     * 逻辑CPU
     */
    private String logic;

    /**
     * cpu使用率
     */
    private String used;

    /**
     * cpu空闲率
     */
    private String idle;
}
