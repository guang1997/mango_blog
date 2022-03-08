package com.myblog.service.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.myblog.service.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author 李斯特
 * @since 2021-10-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_menu")
@ApiModel(value="Menu对象", description="菜单表")
public class Menu extends BaseEntity {

    private static final long serialVersionUID=8559709211352400087L;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单标题")
    private String title;

    @ApiModelProperty(value = "父id")
    private String pid;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序字段，越大越靠前")
    private Integer sort;

    @ApiModelProperty(value = "是否已删除 1:是 0:否")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "是否显示 1:是 0:否")
    private Boolean hidden;

    @ApiModelProperty(value = "组件名称")
    private String component;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "菜单类型 0:目录 1:菜单 2:按钮")
    private String menuType;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "子菜单数量")
    private Integer subCount;

    @TableField(exist = false)
    private List<Menu> children;
}
