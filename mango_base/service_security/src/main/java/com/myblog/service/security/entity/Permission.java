package com.myblog.service.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.myblog.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * API权限表
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_permission")
@ApiModel(value="Permission对象", description="API权限表")
public class Permission extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "API名称")
    private String apiName;

    @ApiModelProperty(value = "API请求地址")
    private String apiUrl;

    @ApiModelProperty(value = "API请求方式")
    private String apiMethod;

    @ApiModelProperty(value = "父ID")
    private String pid;

    @ApiModelProperty(value = "排序")
    private Integer apiSort;

    @ApiModelProperty(value = "描述")
    private String description;


}
