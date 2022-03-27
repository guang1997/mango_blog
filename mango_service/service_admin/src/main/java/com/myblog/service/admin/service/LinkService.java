package com.myblog.service.admin.service;

import com.myblog.service.admin.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.base.common.Response;

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

    Response getLinkByPage(LinkDto linkDto) throws Exception;

    Response addLink(LinkDto linkDto) throws Exception;

    Response editLink(LinkDto linkDto) throws Exception;

    Response delLink(Set<String> ids) throws Exception;
}
