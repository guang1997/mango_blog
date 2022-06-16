package com.myblog.service.base.common;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public enum BehaviorEnum {

    DEFAULT("DEFAULT", "默认行为", 0),
    BLOG_DETAIL("BLOG_DETAIL", "点击博客页面", 1),
    FRIENDSHIP_LINK("FRIENDSHIP_LINK", "点击友情链接页面", 1),
    ARCHIVE("ARCHIVE", "点击归档页面", 1),
    SORT("SORT", "点击博客分类页面", 1),
    TAG("TAG", "点击博客标签页面", 1),
    LIKE("LIKE", "点赞", 0),
    SEARCH("SEARCH", "点击搜索", 0),
    PUBLISH_COMMENT("PUBLISH_COMMENT", "发表评论", 0),
    LOGIN("LOGIN", "登录", 0),
    MESSAGE_BOARD("MESSAGE_BOARD", "点击留言板页面", 1);


    private String behavior;
    private String content;
    private int isMenu;


    BehaviorEnum(String behavior, String content, int isMenu) {
        this.behavior = behavior;
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

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}