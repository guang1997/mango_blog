package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.base.annotation.service.ServiceContextAware;
import com.myblog.service.base.common.Response;

import java.text.ParseException;
import java.util.Set;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
public interface TagService extends IService<Tag>, ServiceContextAware {

    Response getTagByPage(TagDto tagDto) throws Exception;

    Response addTag(TagDto tagDto);

    Response editTag(TagDto tagDto);

    Response delTags(Set<String> ids);
}
