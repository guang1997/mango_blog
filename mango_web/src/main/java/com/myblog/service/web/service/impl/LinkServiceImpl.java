package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.Link;
import com.myblog.service.web.entity.dto.LinkDto;
import com.myblog.service.web.mapper.LinkMapper;
import com.myblog.service.web.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Response getFriendLink(LinkDto linkDto) throws Exception {
        Response response = Response.ok();
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Link.LINK_STATUS, Constants.CommonStatus.ENABLED);
        return response.data(Constants.ReplyField.DATA, this.toDtoList(baseMapper.selectList(queryWrapper), LinkDto.class));
    }
}
