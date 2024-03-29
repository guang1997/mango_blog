package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.controller.LinkController;
import com.myblog.service.web.entity.Link;
import com.myblog.service.web.entity.dto.LinkDto;
import com.myblog.service.web.mapper.LinkMapper;
import com.myblog.service.web.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-06-09
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    private static Logger LOGGER = LoggerFactory.getLogger(LinkServiceImpl.class);

    @Override
    public List<LinkDto> getFriendLink(LinkDto linkDto) throws Exception {
        Response response = Response.ok();
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Link.LINK_STATUS, Constants.CommonStatus.ENABLED);
        return this.toDtoList(baseMapper.selectList(queryWrapper), LinkDto.class);
    }

    @Override
    public Boolean saveFriendLink(LinkDto linkDto) throws Exception {
        Link link = this.toDb(linkDto, Link.class);
        if (baseMapper.insert(link) < 1) {
            LOGGER.error("saveFriendLink failed by unknowen error, link:{}", link);
            return false;
        }
        return true;
    }
}
