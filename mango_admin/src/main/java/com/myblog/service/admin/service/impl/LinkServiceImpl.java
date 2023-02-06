package com.myblog.service.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.service.admin.entity.Link;
import com.myblog.service.admin.entity.dto.LinkDto;
import com.myblog.service.admin.mapper.LinkMapper;
import com.myblog.service.admin.service.LinkService;
import com.myblog.service.base.common.*;
import com.myblog.service.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;


/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-03-24
 */
@Slf4j
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    /**
     * 分页查询友情链接
     * @param linkDto
     * @return
     */
    @Override
    public Map<String, Object> getLinkByPage(LinkDto linkDto) throws Exception {
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();

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
        Page<Link> linkPage = new Page<>(linkDto.getPage(), linkDto.getSize());
        baseMapper.selectPage(linkPage, queryWrapper);
        Map<String, Object> resultMap = new HashMap<>();
        List<LinkDto> dictDtos = this.toDtoList(linkPage.getRecords(), LinkDto.class);
        resultMap.put(Constants.ReplyField.DATA, dictDtos);
        resultMap.put(Constants.ReplyField.TOTAL, linkPage.getTotal());
        resultMap.put(Constants.ReplyField.PAGE, linkDto.getPage());
        resultMap.put(Constants.ReplyField.SIZE, linkDto.getSize());
        return resultMap;
    }

    @Override
    public Boolean addLink(@Validated LinkDto linkDto) throws Exception{
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Link.URL, linkDto.getUrl());
        if (Objects.nonNull(baseMapper.selectOne(queryWrapper))) {
            log.error("addLink failed, url is already exist, link:{}", linkDto);
            return false;
        }
        Link link = this.toDb(linkDto, Link.class);
        // 添加时设置为申请中状态
        link.setLinkStatus(LinkStatusEnum.APPLY.getCode());
        if (baseMapper.insert(link) < 1) {
            log.error("addLink failed by unknown error, link:{}", link);
            return false;
        }
        return true;
    }

    @Override
    public Boolean editLink(LinkDto linkDto) throws Exception{
        QueryWrapper<Link> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DbConstants.Link.URL, linkDto.getUrl());
        List<Link> links = baseMapper.selectList(queryWrapper);
        if (links.size() > 0) {
            for (Link link : links) {
                if (!Objects.equals(link.getId(), linkDto.getId())) {
                    throw new BusinessException("更新失败, 已存在相同路径的友链");
                }
            }
        }
        Link link = this.toDb(linkDto, Link.class);
        if (baseMapper.updateById(link) < 1) {
            log.error("editLink failed by unknown error, link:{}", link);
            return false;
        }
        return true;
    }

    @Override
    public Boolean delLink(Set<String> ids) throws Exception{
        for (String id : ids) {
            if (baseMapper.deleteById(id) < 1) {
                log.error("delLink failed by unknown error, linkId:{}", id);
                return false;
            }
        }
        return true;
    }

}
