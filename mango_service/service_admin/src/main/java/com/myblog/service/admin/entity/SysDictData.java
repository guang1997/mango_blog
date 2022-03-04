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
 * 字典数据表
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_sys_dict_data")
@ApiModel(value="SysDictData对象", description="字典数据表")
public class SysDictData extends BaseEntity {

    private static final long serialVersionUID=8859709211352400087L;

    @ApiModelProperty(value = "字典类型ID")
    private String dictTypeId;

    @ApiModelProperty(value = "字典标签")
    private String dictLabel;

    @ApiModelProperty(value = "字典键值")
    private String dictValue;

    @ApiModelProperty(value = "样式属性（其他样式扩展）")
    private String cssClass;

    @ApiModelProperty(value = "表格回显样式")
    private String listClass;

    @ApiModelProperty(value = "是否默认（1是 0否）,默认为0")
    private Boolean isDefault;

    @ApiModelProperty(value = "创建人id")
    private String createAdminId;

    @ApiModelProperty(value = "最后更新人id")
    private String updateAdminId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "是否发布(1:是，0:否)")
    private String isPublish;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;


}
