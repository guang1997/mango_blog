package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.Tag;
import com.myblog.service.admin.entity.dto.TagDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.common.ResultModel;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-18
 */
public interface TagService extends IService<Tag>, ServiceConvertHandler<Tag, TagDto> {

    Map<String, Object> getTagByPage(TagDto tagDto) throws Exception;

    Boolean addTag(TagDto tagDto) throws Exception;

    Boolean editTag(TagDto tagDto) throws Exception;

    Boolean delTags(Set<String> ids) throws Exception;
}
