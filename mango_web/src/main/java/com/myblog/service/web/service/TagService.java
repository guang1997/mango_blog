package com.myblog.service.web.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.web.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.TagDto;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
public interface TagService extends IService<Tag>, ServiceConvertHandler<Tag, TagDto> {

    Response getTagByPage(TagDto tagDto) throws Exception;
}
