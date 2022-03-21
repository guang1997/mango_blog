package com.myblog.service.admin.entity;

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
 * 博客分类表
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sort")
@ApiModel(value="Sort对象", description="博客分类表")
public class Sort extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "分类名称")
    private String sortName;

    @ApiModelProperty(value = "分类描述")
    private String summary;

    @ApiModelProperty(value = "点击数")
    private Integer clickCount;

    @ApiModelProperty(value = "分类级别")
    private Integer sortLevel;


}
