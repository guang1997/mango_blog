package com.myblog.service.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.myblog.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_user")
@ApiModel(value="User对象", description="用户表")
@ToString(exclude = "password")
public class User extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "性别(1:男2:女)")
    private Integer gender;

    @ApiModelProperty(value = "个人头像")
    private String avatar;

    @ApiModelProperty(value = "出生年月日")
    private Date birthday;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;

    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIp;

    @ApiModelProperty(value = "状态：0-禁用; 1-激活")
    private Integer status;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "QQ号")
    private String qqNumber;

    @ApiModelProperty(value = "微信号")
    private String weChat;

    @ApiModelProperty(value = "评论状态 1:正常 0:禁言")
    private Integer commentStatus;


}
