package com.myblog.service.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.myblog.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 异常信息表
 * </p>
 *
 * @author 李斯特
 * @since 2022-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_exception_log")
@ApiModel(value="ExceptionLog对象", description="异常信息表")
public class ExceptionLog extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "异常信息")
    private String exceptionMessage;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "ip来源")
    private String ipSource;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "异常详细信息")
    private String exceptionDetails;
}
