package com.myblog.service.base.common;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public enum ResultCodeEnum {

    SUCCESS(true, 20000,"成功"),
    UNKNOWN_REASON(false, 20001, "失败"),
    QUERY_FAILED(false, 20003, "查询失败"),
    UPDATE_FAILED(false, 20005, "更新失败"),
    SAVE_FAILED(false, 20007, "保存失败"),
    DELETE_FAILED(false, 20009, "删除失败"),
    LOGIN_ERROR(false, 23002, "登录失败"),
    LOGOUT_ERROR(false, 23004, "退出登录失败"),
    LOGIN_ERROR_LOCKED(false, 23005, "用户名或密码错误，错误%d次后，账户将被锁定30分钟"),
    LOGIN_ERROR_ROLE(false, 23006, "没有分配角色权限"),
    LOGIN_ERROR_BY_TOKEN_EXPIRED(false, 23007, "令牌已过期，请重新登录"),
    LOGIN_ERROR_BY_VALIDATA_TOKEN(false, 23008, "无效的令牌"),
    LOGIN_LOCKED(false, 23009, "密码输错次数过多,已被锁定30分钟"),
    GET_USERINFO_ERROR(false, 23010, "获取用户信息失败"),
    GET_USERMENU_ERROR(false, 23011, "获取用户菜单失败"),
    CODE_ERROR(false, 23012, "验证码错误"),
    UPLOAD_ERROR(false, 23013, "上传失败"),
    UPDATE_FAILED_BY_QQ_NUMBER_EXIST(false, 23014, "更新失败，QQ号已存在"),
    UPDATE_FAILED_BY_WE_CHAT_EXIST(false, 23015, "更新失败，微信号已存在"),
    UPDATE_FAILED_BY_PHONE_EXIST(false, 23016, "更新失败，手机号已存在"),
    SEND_CODE_FAILED(false, 23017, "发送验证码失败"),
    COMMENT_FAILED(false, 23018, "评论失败"),
    SEND_EMAIL_FAILED(false, 23019, "邮件发送失败"),
//    SAVE_TAG_FAILED(false, 200010, "保存失败，已经存在对应标签"),
//    SAVE_TYPE_FAILED(false, 200011, "保存失败，已经存在对应类型"),
//
//    DELETE_TAG_FAILED(false, 200012, "删除失败，正在被使用的标签"),
//    DELETE_TYPE_FAILED(false, 200013, "删除失败，正在被使用的标签"),
//
//    BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
//    JSON_PARSE_ERROR(false, 21002, "json解析异常"),
//    PARAM_ERROR(false, 21003, "参数不正确"),

//    FILE_UPLOAD_ERROR(false, 21004, "文件上传错误"),
//    FILE_DELETE_ERROR(false, 21005, "文件刪除错误"),
//    EXCEL_DATA_IMPORT_ERROR(false, 21006, "Excel数据导入错误"),
//
//    VIDEO_UPLOAD_ALIYUN_ERROR(false, 22001, "视频上传至阿里云失败"),
//    VIDEO_UPLOAD_TOMCAT_ERROR(false, 22002, "视频上传至业务服务器失败"),
//    VIDEO_DELETE_ALIYUN_ERROR(false, 22003, "阿里云视频文件删除失败"),
//    FETCH_VIDEO_UPLOADAUTH_ERROR(false, 22004, "获取上传地址和凭证失败"),
//    REFRESH_VIDEO_UPLOADAUTH_ERROR(false, 22005, "刷新上传地址和凭证失败"),
//    FETCH_PLAYAUTH_ERROR(false, 22006, "获取播放凭证失败"),
//
//    URL_ENCODE_ERROR(false, 23001, "URL编码失败"),
//    ILLEGAL_CALLBACK_REQUEST_ERROR(false, 23002, "非法回调请求"),
//    FETCH_ACCESSTOKEN_FAILD(false, 23003, "获取accessToken失败"),
//    FETCH_USERINFO_ERROR(false, 23004, "获取用户信息失败"),


//    COMMENT_EMPTY(false, 24006, "评论内容必须填写"),
//
//    PAY_RUN(false, 25000, "支付中"),
//    PAY_UNIFIEDORDER_ERROR(false, 25001, "统一下单错误"),
//    PAY_ORDERQUERY_ERROR(false, 25002, "查询支付结果错误"),
//
//    ORDER_EXIST_ERROR(false, 25003, "课程已购买"),
//
//    GATEWAY_ERROR(false, 26000, "服务不能访问"),




//    LOGIN_USERNAME_OR_PASSWORD_ERROR(false, 28010, "账号或密码不正确"),
//    LOGIN_PHONE_ERROR(false, 28009, "手机号码不正确"),
//    LOGIN_MOBILE_ERROR(false, 28001, "账号不正确"),
//    LOGIN_PASSWORD_ERROR(false, 28008, "密码不正确"),
//    LOGIN_DISABLED_ERROR(false, 28002, "该用户已被禁用"),
//    REGISTER_MOBLE_ERROR(false, 28003, "手机号已被注册"),
//    LOGIN_AUTH(false, 28004, "需要登录"),
//    LOGIN_ACL(false, 28005, "没有权限"),
//    SMS_SEND_ERROR(false, 28006, "短信发送失败"),
//    SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL(false, 28007, "短信发送过于频繁"),
//    REMOTE_CALL_ERROR(false, 29001, "远程调用失败")
    /*,
    YOU_DEFINE(false, 299999, "你的错误消息")*/;

    private Boolean success;

    private Integer code;

    private String message;

    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}