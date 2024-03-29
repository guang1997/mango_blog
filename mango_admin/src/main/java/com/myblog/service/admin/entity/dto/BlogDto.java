package com.myblog.service.admin.entity.dto;

import com.myblog.service.admin.entity.Tag;
import com.myblog.service.base.entity.dto.BaseReqDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BlogDto extends BaseReqDto {

    @NotNull(message = "博客标题不能为空")
    private String title;

    @NotNull(message = "博客简介不能为空")
    private String summary;

    @NotNull(message = "博客内容不能为空")
    private String content;

    private Integer clickCount;

    private Integer likeCount;

    @NotNull(message = "博客标题图不能为空")
    private String fileId;

    private String adminId;

    private String author;

    @NotNull(message = "博客分类不能为空")
    private String blogSortId;

    private String sortName;

    private String tagId;

    @NotNull(message = "博客标签不能为空")
    private List<Tag> tags;


    private Long ids123;
}
