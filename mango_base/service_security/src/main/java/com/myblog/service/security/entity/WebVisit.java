package com.myblog.service.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.myblog.service.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * Web访问记录表
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_web_visit")
@ApiModel(value="WebVisit对象", description="Web访问记录表")
public class WebVisit extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "访问ip地址")
    private String ip;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "浏览器")
    private String browser;

    @ApiModelProperty(value = "ip来源")
    private String ipSource;

    @ApiModelProperty(value = "用户行为")
    private String behavior;

    @ApiModelProperty(value = "行为内容")
    private String content;

    @ApiModelProperty(value = "请求时间")
    private Date requestTime;

    @ApiModelProperty(value = "是否是菜单1:是0:否")
    private Integer isMenu;


}
