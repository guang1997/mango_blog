package com.myblog.service.web.service;

import com.myblog.service.base.common.Response;
import com.myblog.service.base.handler.ServiceConvertHandler;
import com.myblog.service.web.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.service.web.entity.dto.LinkDto;

/**
 * <p>
 * 友情链接表 服务类
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-09
 */
public interface LinkService extends IService<Link>, ServiceConvertHandler<Link, LinkDto> {

    Response getFriendLink(LinkDto linkDto) throws Exception;

    Response saveFriendLink(LinkDto linkDto) throws Exception;
}
