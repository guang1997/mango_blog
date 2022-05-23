package com.myblog.service.base.common;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public enum CommentSourceEnum {

    BLOG_INFO_LIKES("BLOG_INFO_LIKES", 1, "博客详情页面点赞"),
    BLOG_INFO_MESSAGE("BLOG_INFO_MESSAGE", 0, "博客详情页面评论"),
    BLOG_INFO_COMMENT_LIKES("BLOG_INFO_COMMENT_LIKES", 1, "博客详情页面评论点赞"),
    ABOUT_LIKES("ABOUT_LIKES", 1, "关于我页面点赞"),
    ABOUT_MESSAGE("ABOUT_MESSAGE", 0, "关于我页面评论"),
    MESSAGE_BOARD_LIKES("MESSAGE_BOARD_LIKES", 1, "留言板页面点赞"),
    MESSAGE_BOARD_MESSAGE("MESSAGE_BOARD_MESSAGE", 0, "留言板页面评论"),

    ;

    private String source;
    private int commentType;
    private String chineseName;

    private static Map<String, CommentSourceEnum> sourceMap = new HashMap<>();

    CommentSourceEnum(String source, int commentType, String chineseName) {
        this.source = source;
        this.commentType = commentType;
        this.chineseName = chineseName;
    }

    static {
        for (CommentSourceEnum sourceEnum : values()) {
            sourceMap.put(sourceEnum.getSource(), sourceEnum);
        }
    }

    public static CommentSourceEnum getCommentSourceEnumBySource(String source) {
        return StringUtils.isBlank(source) ? null : sourceMap.get(source);
    }
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }
}