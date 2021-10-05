package com.myblog.service.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 菜单表
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_menu")
@ApiModel(value="Menu对象", description="菜单表")
public class Menu extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单级别")
    private Boolean menuLevel;

    @ApiModelProperty(value = "简介")
    private String summary;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "url地址")
    private String url;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序字段，越大越靠前")
    private Integer sort;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "是否显示 1:是 0:否")
    @TableField("is_show")
    private Boolean show;

    @ApiModelProperty(value = "菜单类型 0: 菜单   1: 按钮")
    private Boolean menuType;

    @ApiModelProperty(value = "是否跳转外部链接 0：否，1：是")
    @TableField("is_jump_external_url")
    private Boolean jumpExternalUrl;


}
