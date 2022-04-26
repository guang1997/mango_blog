package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.admin.entity.Link;
import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.admin.mapper.LinkMapper;
import com.myblog.service.admin.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.base.common.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    private static Logger LOGGER = LoggerFactory.getLogger(LinkServiceImpl.class);
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
                    .like(DbConstants.Base.SUMMARY, linkDto.getBlurry());
        }
        if (StringUtils.isNotBlank(linkDto.getUrl())) {
            queryWrapper.like(DbConstants.Link.URL, linkDto.getUrl());
        }
        if (Objects.nonNull(linkDto.getLinkStatus())) {
            queryWrapper.eq(DbConstants.Link.LINK_STATUS, linkDto.getLinkStatus());
        }
        queryWrapper.orderByDesc(DbConstants.Base.UPDATE_TIME);
        Page<Link> linkPage = new Page<>(page, size);
        baseMapper.selectPage(linkPage, queryWrapper);
        List<LinkDto> dictDtos = this.toDtoList(linkPage.getRecords(), LinkDto.class);
        response.data(Constants.ReplyField.DATA, dictDtos);
        response.data(Constants.ReplyField.TOTAL, linkPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }

    @Override
    public Response addLink(LinkDto linkDto) throws Exception{
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Link.URL, linkDto.getUrl());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            LOGGER.error("addLink failed, url is already exist, link:{}", linkDto);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        Link link = this.toDb(linkDto, Link.class);
        // 添加时设置为申请中状态
        link.setLinkStatus(LinkStatusEnum.APPLY.getCode());
        if (baseMapper.insert(link) < 1) {
            LOGGER.error("addLink failed by unknown error, link:{}", link);
            return Response.setResult(ResultCodeEnum.SAVE_FAILED);
        }
        return Response.ok();
    }

    @Override
    public Response editLink(LinkDto linkDto) throws Exception{
        Link link = this.toDb(linkDto, Link.class);
        if (baseMapper.updateById(link) < 1) {
            LOGGER.error("editLink failed by unknown error, link:{}", link);
            return Response.setResult(ResultCodeEnum.UPDATE_FAILED);
        }
        return Response.ok();
    }

    @Override
    public Response delLink(Set<String> ids) throws Exception{
        for (String id : ids) {
            if (baseMapper.deleteById(id) < 1) {
                LOGGER.error("delLink failed by unknown error, linkId:{}", id);
                return Response.setResult(ResultCodeEnum.DELETE_FAILED);
            }
        }
        return Response.ok();
    }

}
