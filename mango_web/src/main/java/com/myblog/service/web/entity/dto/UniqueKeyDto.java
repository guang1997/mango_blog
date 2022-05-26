package com.myblog.service.web.entity.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UniqueKeyDto {

    /**
     * 操作系统名
     */
    private String name;

    /**
     * 访问设备类型
     */
    private String deviceType;

    /**
     * 操作系统家族
     */
    private String group;

    /**
     * 操作系统生产厂商
     */
    private String manufacturer;
    /**
     * 分辨率高
     */
    private Integer height;

    /**
     * 分辨率宽
     */
    private Integer width;

    /**
     * 图像每英寸长度内的像素点数
     */
    private Integer dpi;

    /**
     * 像素深度
     */
    private Integer pixelDepth;
}
