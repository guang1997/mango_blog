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
 * 评论表
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_comment")
@ApiModel(value="Comment对象", description="评论表")
public class Comment extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "评论来源ip")
    private String ip;

    @ApiModelProperty(value = "唯一标识，如果用户id不同，ip相同，则判断此字段")
    private String uniqueKey;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等")
    private String source;

    @ApiModelProperty(value = "评论类型 1:点赞 0:评论")
    private Integer type;

    @ApiModelProperty(value = "博客id")
    private String blogId;

    @ApiModelProperty(value = "状态: 0-待审核, 1-审核通过, 2-审核不通过")
    private Integer status;


}
