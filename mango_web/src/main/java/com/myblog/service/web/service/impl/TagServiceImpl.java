package com.myblog.service.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.service.base.common.Constants;
import com.myblog.service.base.common.DbConstants;
import com.myblog.service.base.common.Response;
import com.myblog.service.web.entity.Tag;
import com.myblog.service.web.entity.dto.TagDto;
import com.myblog.service.web.mapper.TagMapper;
import com.myblog.service.web.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author 李斯特
 * @since 2022-05-19
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public Response getTagByPage(TagDto tagDto) throws Exception{
        Response response = Response.ok();
        if (Objects.nonNull(tagDto.getQueryAll()) && tagDto.getQueryAll()) {
            List<TagDto> tagDtos = this.toDtoList(baseMapper.selectList(null), TagDto.class);
            response.data(Constants.ReplyField.DATA, tagDtos);
            response.data(Constants.ReplyField.TOTAL, tagDtos.size());
            return response;
        }
        int page = 1;
        int size = 1000;
        if (Objects.nonNull(tagDto.getPage())) page = tagDto.getPage();
        if (Objects.nonNull(tagDto.getSize())) size = tagDto.getSize();

        Page<Tag> tagPage = new Page<>(page, size);
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DbConstants.Base.CREATE_TIME);
        baseMapper.selectPage(tagPage, queryWrapper);

        response.data(Constants.ReplyField.DATA, this.toDtoList(tagPage.getRecords(), TagDto.class));
        response.data(Constants.ReplyField.TOTAL, tagPage.getTotal());
        response.data(Constants.ReplyField.PAGE, page);
        response.data(Constants.ReplyField.SIZE, size);
        return response;
    }
}
