package com.myblog.service.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.Link;
import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 友情链接表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-24
 */
public interface LinkService extends IService<Link>, ServiceConvertHandler<Link, LinkDto> {

    Map<String, Object> getLinkByPage(LinkDto linkDto) throws Exception;

    Boolean addLink(LinkDto linkDto) throws Exception;

    Boolean editLink(LinkDto linkDto) throws Exception;

    Boolean delLink(Set<String> ids) throws Exception;
}
