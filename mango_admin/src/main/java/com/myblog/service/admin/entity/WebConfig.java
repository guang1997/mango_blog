package com.myblog.service.admin.entity;

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
 * web配置表
 * </p>
 *
 * @author 李斯特
 * @since 2023-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_web_config")
@ApiModel(value="WebConfig对象", description="web配置表")
public class WebConfig extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "网站名称")
    private String name;

    @ApiModelProperty(value = "介绍")
    private String summary;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "备案号")
    private String recordNum;

    @ApiModelProperty(value = "github地址")
    private String github;

    @ApiModelProperty(value = "gitee地址")
    private String gitee;

    @ApiModelProperty(value = "首页滚动句子")
    private String rollingSentences;

    @ApiModelProperty(value = "友链简介")
    private String friendLinkDesc;

    @ApiModelProperty(value = "友链地址")
    private String friendLinkUrl;
}
