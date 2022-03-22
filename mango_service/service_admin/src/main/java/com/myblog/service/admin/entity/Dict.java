package com.myblog.service.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.myblog.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_dict")
@ApiModel(value="Dict对象", description="字典表")
public class Dict extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "备注")
    private String summary;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "排序字段，越大越靠前")
    private Integer sort;


}
