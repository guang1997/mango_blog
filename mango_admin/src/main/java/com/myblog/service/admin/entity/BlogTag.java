package com.myblog.service.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.myblog.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_blog_tag")
@ApiModel(value="BlogTag对象", description="")
public class BlogTag extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "博客ID")
    private String blogId;

    @ApiModelProperty(value = "标签ID")
    private String tagId;


}
