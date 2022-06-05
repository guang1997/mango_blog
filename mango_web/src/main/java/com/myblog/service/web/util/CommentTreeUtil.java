package com.myblog.service.web.util;



import com.myblog.service.base.common.CommentSourceEnum;
import com.myblog.service.base.common.Constants;
import com.myblog.service.security.entity.Menu;
import com.myblog.service.security.entity.dto.MenuDto;
import com.myblog.service.web.entity.dto.CommentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 递归封装评论
 *
 * @author 李斯特
 * @date 2021/09/28
 */
public class CommentTreeUtil {

    public static List<CommentDto> toCommentTree(List<CommentDto> treeList, String parentId) {
        List<CommentDto> retList = new ArrayList<>();
        for (CommentDto parent : treeList) {
            if (parentId.equals(parent.getParentId())) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }

    private static CommentDto findChildren(CommentDto parent, List<CommentDto> treeList) {
        for (CommentDto child : treeList) {
            if (parent.getId().equals(child.getParentId())) {
                if (Objects.equals(child.getType(), Constants.CommentType.LIKES)) {
                    if (Objects.equals(child.getContent(), "true")) {
                        parent.setLiked(true);
                    }
                    continue;
                }
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(findChildren(child, treeList));
            }
        }
        return parent;
    }
}