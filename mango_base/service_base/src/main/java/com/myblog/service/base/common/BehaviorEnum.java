package com.myblog.service.base.common;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public enum BehaviorEnum {

    BLOG_TAG("点击标签", 0),
    BLOG_SORT("点击博客分类", 0),
    VISIT_PAGE("点击首页", 1),
    BLOG_CONTNET("点击博客", 1),
    FRIENDSHIP_LINK("点击友情链接", 1),
    VISIT_SORT("点击归档", 1),
    BLOG_AUTHOR("点击作者", 1),
    VISIT_CLASSIFY("点击博客分类页面", 1),
    VISIT_TAG("点击博客标签页面", 1),
    BLOG_PRAISE("点赞", 0),
    BLOG_SEARCH("点击搜索", 0),
    PUBLISH_COMMENT("发表评论", 0),
    DELETE_COMMENT("删除评论", 0),
    REPORT_COMMENT("举报评论", 0);


    private String content;
    private int isMenu;


    BehaviorEnum(String content, int isMenu) {
        this.content = content;
        this.isMenu = isMenu;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(int isMenu) {
        this.isMenu = isMenu;
    }

    public static List<BehaviorEnum> getMenuBehaviors() {
        List<BehaviorEnum> resultList = new ArrayList<>();
        for (BehaviorEnum behaviorEnum : BehaviorEnum.values()) {
            if (Objects.equals(behaviorEnum.getIsMenu(), 1)) {
                resultList.add(behaviorEnum);
            }
        }
        return resultList;
    }
}