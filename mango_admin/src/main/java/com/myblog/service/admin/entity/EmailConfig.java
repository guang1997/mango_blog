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
 * 邮箱配置
 * </p>
 *
 * @author 李斯特
 * @since 2022-04-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_email_config")
@ApiModel(value="EmailConfig对象", description="邮箱配置")
public class EmailConfig extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "发件人")
    private String fromUser;

    @ApiModelProperty(value = "邮件服务器SMTP地址")
    private String host;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "端口")
    private Integer port;

    @ApiModelProperty(value = "发件者用户名")
    private String user;

    @ApiModelProperty(value = "邮件主题")
    private String subject;


}
