package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.admin.entity.Link;
import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.admin.mapper.LinkMapper;
import com.myblog.service.admin.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.annotation.service.ServiceContext;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.base.entity.dto.BaseDto;
import com.myblog.service.base.handler.ServiceConvertHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;




/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-24
 */
@Service
@ServiceContext(serviceName = "LinkService", dbClazz = Link.class, dtoClazz = LinkDto.class)
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Autowired
    private ServiceConvertHandler serviceConvertHandler;

    /**
     * 分页查询友情链接
     * @param linkDto
     * @return
     */
    @Override
    public Response getLinkByPage(LinkDto linkDto) throws Exception {
        Response response = Response.ok();
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();

        int page = 1;
        int size = 10;
        if (Objects.nonNull(linkDto.getPage())) page = linkDto.getPage();
        if (Objects.nonNull(linkDto.getSize())) size = linkDto.getSize();

        if (StringUtils.isNotBlank(linkDto.getBlurry())) {
            queryWrapper.like(DbConstants.Link.TITLE, linkDto.getBlurry())
                    .or()
                    .like(DbConstants.Base.SUMMARY, linkDto.getBlurry())
                    .or()
                    .like(DbConstants.Link.URL, linkDto.getUrl());
        }
        queryWrapper.orderByDesc(DbConstants.Base.UPDATE_TIME);
        Page<Link> linkPage = new Page<>(page, size);
        baseMapper.selectPage(linkPage, queryWrapper);
        List<BaseDto> dictDtos = serviceConvertHandler.toDto(linkPage.getRecords(), this.getServiceName());
        response.data(Constants.ReplyField.DATA, dictDtos);
        response.data(Constants.ReplyField.TOTAL, linkPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    @Override
    public Response addLink(LinkDto linkDto) {
        return null;
    }

    @Override
    public Response editLink(LinkDto linkDto) {
        return null;
    }

    @Override
    public Response delLink(Set<String> ids) {
        return null;
    }

}
