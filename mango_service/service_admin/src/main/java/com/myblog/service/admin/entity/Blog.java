package com.myblog.service.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.myblog.service.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2021-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_blog")
@ApiModel(value="Blog对象", description="博客表")
public class Blog extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "博客标题")
    private String title;

    @ApiModelProperty(value = "博客简介")
    private String summary;

    @ApiModelProperty(value = "博客内容")
    private String content;

    @ApiModelProperty(value = "标签id")
    private Integer tagId;

    @ApiModelProperty(value = "博客点击数")
    private Integer clickCount;

    @ApiModelProperty(value = "博客收藏数")
    private Integer collectCount;

    @ApiModelProperty(value = "标题图片id")
    private String fileId;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "管理员id")
    private Integer adminId;

    @ApiModelProperty(value = "是否原创（0:不是 1：是）")
    private String isOriginal;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "文章出处")
    private String articlesPart;

    @ApiModelProperty(value = "博客分类ID")
    private Integer blogSortId;

    @ApiModelProperty(value = "推荐等级(0:正常)")
    private Boolean level;

    @ApiModelProperty(value = "是否发布：0：否，1：是")
    private String isPublish;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "是否开启评论(0:否 1:是)")
    private Boolean openComment;

    @ApiModelProperty(value = "类型【0 博客， 1：推广】")
    private Boolean type;

    @ApiModelProperty(value = "外链【如果是推广，那么将跳转到外链】")
    private String outsideLink;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
