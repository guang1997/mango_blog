package com.myblog.service.security.util;



import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.security.entity.vo.MenuVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 递归封装左侧菜单栏
 *
 * @author 李斯特
 * @date 2021/09/28
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

    public static List<Menu> toMenuTree(List<Menu> treeList, String pid) {
        List<Menu> retList = new ArrayList<>();
        for (Menu parent : treeList) {
            if (pid.equals(parent.getPid())) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }

    private static Menu findChildren(Menu parent, List<Menu> treeList) {
        for (Menu child : treeList) {
            if (parent.getId().equals(child.getPid())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(findChildren(child, treeList));
            }
        }
        return parent;
    }

    public static List<MenuDto> toMenuDtoTree(List<MenuDto> treeList, String pid) {
        List<MenuDto> retList = new ArrayList<>();
        for (MenuDto parent : treeList) {
            if (pid.equals(parent.getPid())) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }

    private static MenuDto findChildren(MenuDto parent, List<MenuDto> treeList) {
        for (MenuDto child : treeList) {
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