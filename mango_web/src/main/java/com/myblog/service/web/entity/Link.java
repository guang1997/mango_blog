package com.myblog.service.web.entity;

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
 * 友情链接表
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_link")
@ApiModel(value="Link对象", description="友情链接表")
public class Link extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "友情链接标题")
    private String title;

    @ApiModelProperty(value = "友情链接介绍")
    private String summary;

    @ApiModelProperty(value = "友情链接URL")
    private String url;

    @ApiModelProperty(value = "友链状态： 0 申请中， 1：已上线")
    private Integer linkStatus;

    @ApiModelProperty(value = "邮箱")
    private String email;
}
