package com.myblog.service.admin.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.myblog.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 博客表
 * </p>
 *
 * @author 李斯特
 * @since 2021-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_blog")
@ApiModel(value="Blog对象", description="博客表")
public class Blog extends BaseEntity {

    private static final long serialVersionUID=8759709211352400087L;

    @ApiModelProperty(value = "博客标题")
    private String title;

    @ApiModelProperty(value = "博客简介")
    private String summary;

    @ApiModelProperty(value = "博客内容")
    private String content;

    @ApiModelProperty(value = "博客点击数")
    private Integer clickCount;

    @ApiModelProperty(value = "博客点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "标题图片id")
    private String fileId;

    @ApiModelProperty(value = "管理员id")
    private String adminId;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "博客分类ID")
    private String blogSortId;

    @ApiModelProperty(value = "推荐等级(0:正常)")
    private Integer level;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "是否开启评论(0:否 1:是)")
    private Integer openComment;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
