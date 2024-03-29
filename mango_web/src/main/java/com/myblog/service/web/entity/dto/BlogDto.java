package com.myblog.service.web.entity.dto;

import com.myblog.service.base.entity.dto.BaseReqDto;
import com.myblog.service.web.entity.Comment;
import com.myblog.service.web.entity.Sort;
import com.myblog.service.web.entity.Tag;
import com.myblog.service.web.entity.dto.BlogDto.BlogValidGroup.GetBlogBySortId;
import com.myblog.service.web.entity.dto.BlogDto.BlogValidGroup.GetBlogByTagId;
import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotNull;

@Data
public class BlogDto extends BaseReqDto {

    private String title;

    private String summary;

    private String content;

    private Integer clickCount;

    private Integer likeCount;

    private String fileId;

    private String adminId;

    private String author;

    private Sort sort;

    private List<Tag> tags;

    @NotNull(message = "分类id不能为空", groups = {GetBlogBySortId.class})
    private String blogSortId;

    @NotNull(message = "标签id不能为空", groups = {GetBlogByTagId.class})
    private String tagId;

    private List<Comment> comments;

    private Boolean liked;

    private String userId;

    private UniqueKeyDto screenInformation;

    private String browserFinger;

    private String keyword;

    public interface BlogValidGroup {
        interface GetBlogBySortId{}

        interface GetBlogByTagId{}
    }
}
