package com.myblog.service.admin.util;


import com.myblog.service.admin.entity.vo.MenuVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 递归封装左侧菜单栏
 */
public class TreeUtil {

    public static List<MenuVo> toTree(List<MenuVo> treeList, String pid) {
        List<MenuVo> retList = new ArrayList<>();
        for (MenuVo parent : treeList) {
            if (pid.equals(parent.getPid())) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }

    private static MenuVo findChildren(MenuVo parent, List<MenuVo> treeList) {
        for (MenuVo child : treeList) {
            if (parent.getId().equals(child.getPid())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(findChildren(child, treeList));
            }
        }
        return parent;
    }
}