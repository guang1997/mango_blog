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
 * 字典类型表
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sys_dict_type")
@ApiModel(value="SysDictType对象", description="字典类型表")
public class SysDictType extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "创建人id")
    private String createAdminUid;

    @ApiModelProperty(value = "最后更新人id")
    private String updateAdminUid;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;


    @ApiModelProperty(value = "排序字段")
    private Integer sort;


}
